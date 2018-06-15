package com.permoji.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.permoji.api.trait.Trait;
import com.permoji.cache.LocalDatabase;
import com.permoji.notifications.UserNotification;
import com.permoji.notifications.UserNotificationRepository;
import com.permoji.trait.data.Notifier;
import com.permoji.trait.TraitDefinitionBuilder;
import com.permoji.user.UserTraitsRepository;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by michael on 01/06/18.
 */

public class NotificationReceiver extends BroadcastReceiver {

    public Context context;
    public Intent intent;
    public PendingResult pendingResult;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        this.context = context;
        this.intent = intent;

        if(intent.getExtras().getString("broadcastType") != null) {
            this.pendingResult = goAsync();

            if (intent.getExtras().getString("broadcastType").equals("keyboardBroadcast")) {
                new CreateTraitDefinitionAsyncTask(this).execute();
            }
            else {
//                        new UpdateCacheAsyncTask(this).execute();
                new UpdateTraitDefinitionAsyncTask(this).execute();
            }
        }


    }

    private static class CreateTraitDefinitionAsyncTask extends AsyncTask<String, Void, Void> {

        private WeakReference<NotificationReceiver> notificationReceiverWeakReference;
        private PendingResult pendingResult;
        private Integer emojiCodepoint;
        private TraitDefinitionBuilder traitDefinitionBuilder;

        public CreateTraitDefinitionAsyncTask(NotificationReceiver notificationReceiver) {
            notificationReceiverWeakReference = new WeakReference<>(notificationReceiver);
            Bundle bundle = notificationReceiverWeakReference.get().intent.getExtras();
            pendingResult = notificationReceiverWeakReference.get().pendingResult;
            emojiCodepoint = bundle.getInt("emojiCodepoint");
            traitDefinitionBuilder = new TraitDefinitionBuilder(notificationReceiverWeakReference.get().context);
        }

        @Override
        protected  void onPreExecute() {
            //Ensure db has built before executing
            LocalDatabase.getInstance(notificationReceiverWeakReference.get().context);
        }

        @Override
        protected Void doInBackground(String... strings) {

            traitDefinitionBuilder.createTrait(emojiCodepoint);

            pendingResult.finish();
            return null;
        }
    }

    private static class UpdateTraitDefinitionAsyncTask extends AsyncTask<String, Void, Void> {

        private WeakReference<NotificationReceiver> notificationReceiverWeakReference;
        private PendingResult pendingResult;
        private String notificationImagePath;
        private String notifierName;
        private ArrayList<Integer> emojiCodepoints;
        private TraitDefinitionBuilder traitDefinitionBuilder;

        public UpdateTraitDefinitionAsyncTask(NotificationReceiver notificationReceiver) {
            notificationReceiverWeakReference = new WeakReference<>(notificationReceiver);
            Bundle bundle = notificationReceiverWeakReference.get().intent.getExtras();
            pendingResult = notificationReceiverWeakReference.get().pendingResult;
            notificationImagePath = bundle.getString("filePath");
            emojiCodepoints = bundle.getIntegerArrayList("emojiCodepoints");
            notifierName = bundle.getString("name");

            traitDefinitionBuilder = new TraitDefinitionBuilder(notificationReceiverWeakReference.get().context);
        }

        @Override
        protected  void onPreExecute() {
            //Ensure db has built before executing
            LocalDatabase.getInstance(notificationReceiverWeakReference.get().context);
        }


        @Override
        protected Void doInBackground(String... strings) {

            addNotifierToRecentTrait();

            pendingResult.finish();
            return null;
        }

        private void addNotifierToRecentTrait() {
            Notifier notifier = new Notifier();
            notifier.setImagePath(notificationImagePath);
            notifier.setName(notifierName);

            for(Integer codepoint : emojiCodepoints) {
                traitDefinitionBuilder.addNotifierToRecentTrait(codepoint, notifier);
            }
        }
    }

    private static class UpdateCacheAsyncTask extends AsyncTask<String, Void, Void> {

        private WeakReference<NotificationReceiver> notificationReceiverWeakReference;
        private UserTraitsRepository userTraitsRepository;
        private UserNotificationRepository userNotificationRepository;
        private Bundle bundle;
        private PendingResult pendingResult;
        private String notificationImagePath;
        private ArrayList<Integer> emojiCodepoints;

        public UpdateCacheAsyncTask(NotificationReceiver notificationReceiver) {
            notificationReceiverWeakReference = new WeakReference<>(notificationReceiver);
            bundle = notificationReceiverWeakReference.get().intent.getExtras();
            pendingResult = notificationReceiverWeakReference.get().pendingResult;
            notificationImagePath = bundle.getString("filePath");
            emojiCodepoints = bundle.getIntegerArrayList("emojiCodepoints");
        }

        @Override
        protected Void doInBackground(String... strings) {

            userTraitsRepository = new UserTraitsRepository(notificationReceiverWeakReference.get().context);
            userNotificationRepository = new UserNotificationRepository(notificationReceiverWeakReference.get().context);

            List<Trait> cachedTraits = userTraitsRepository.getAllTraits();

            Log.d(this.getClass().getSimpleName(), "Processing broadcast");
            if (bundle != null) {

                for (int codepoint : emojiCodepoints) {

                    writeTraitToCache(codepoint, cachedTraits, notificationImagePath);
                    writeNotificationToCache(codepoint, notificationImagePath);
                }
            }
            pendingResult.finish();
            return null;
        }

        private void writeTraitToCache(int traitCodepoint, List<Trait> cachedTraits, String notificationImagePath) {
            Trait traitToWrite = null;

            for(Trait cachedTrait : cachedTraits) {
                if(cachedTrait.getCodepoint() == traitCodepoint) {
                    //Update existing trait
                    ArrayList<String> imagePaths = cachedTrait.getVoucherImageNames();
                    if(!imagePaths.contains(notificationImagePath)) {
                        imagePaths.add(notificationImagePath);
                        cachedTrait.setVoucherImageNames(imagePaths);
                    }
                    cachedTrait.setAmount(cachedTrait.getAmount() + 1);
                    userTraitsRepository.update(cachedTrait);
                    return;
                }
            }

            //Otherwise add new trait
            traitToWrite = new Trait();
            traitToWrite.setCodepoint(traitCodepoint);
            //TODO: Implement actual trait description
            traitToWrite.setDescription("Trait");
            traitToWrite.setAmount(1);
            ArrayList<String> v = new ArrayList<>(); v.add(notificationImagePath);
            traitToWrite.setVoucherImageNames(v);


            userTraitsRepository.insert(traitToWrite);

        }

        private void writeNotificationToCache(int TraitCodepoint, String notificationImagePath) {

            UserNotification userNotification = new UserNotification();
            userNotification.setDetail("reacted with");
            userNotification.setImagePath(notificationImagePath);
            userNotification.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            userNotification.setTraitName("Cheerful");
            userNotification.setTraitCodepoint(TraitCodepoint);

            userNotificationRepository.insertUserNotificationAsync(userNotification);
        }
    };
}
