package com.permoji.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.permoji.api.trait.Trait;
import com.permoji.notifications.UserNotification;
import com.permoji.notifications.UserNotificationRepository;
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

        this.pendingResult = goAsync();
        new AccessCacheAsyncTask(this).execute();

    }

    private static class AccessCacheAsyncTask extends AsyncTask<String, Void, Void> {

        private WeakReference<NotificationReceiver> notificationReceiverWeakReference;
        private UserTraitsRepository userTraitsRepository;
        private UserNotificationRepository userNotificationRepository;

        public AccessCacheAsyncTask(NotificationReceiver notificationReceiver) {
            notificationReceiverWeakReference = new WeakReference<>(notificationReceiver);
        }

        @Override
        protected Void doInBackground(String... strings) {

            if(notificationReceiverWeakReference.get() != null) {
                userTraitsRepository = new UserTraitsRepository(notificationReceiverWeakReference.get().context);
                userNotificationRepository = new UserNotificationRepository(notificationReceiverWeakReference.get().context);

                List<Trait> cachedTraits = userTraitsRepository.getAllTraits();

                Bundle bundle = notificationReceiverWeakReference.get().intent.getExtras();
                Log.d(this.getClass().getSimpleName(), "Received broadcast");
                if (bundle != null) {

                    String notificationImagePath = bundle.getString("filePath");
                    ArrayList<Integer> emojiCodepoints = bundle.getIntegerArrayList("emojiCodepoints");

                    for (int codepoint : emojiCodepoints) {

                        writeTraitToCache(codepoint, cachedTraits, notificationImagePath);
                        writeNotificationToCache(codepoint, notificationImagePath);
                    }
                }


                notificationReceiverWeakReference.get().pendingResult.finish();
            }
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
