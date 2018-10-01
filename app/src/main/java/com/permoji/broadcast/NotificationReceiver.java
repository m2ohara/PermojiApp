package com.permoji.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.permoji.database.LocalDatabase;
import com.permoji.model.entity.Notifier;
import com.permoji.builder.TraitDefinitionBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by michael on 01/06/18.
 */

public class NotificationReceiver extends BroadcastReceiver {

    public Context context;
    public Intent intent;
    public PendingResult pendingResult;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.i(this.getClass().getSimpleName(), "Received broadcasted intent "+intent.getExtras().getString("name"));

        this.context = context;
        this.intent = intent;

        if(intent.getExtras().getString("broadcastType") != null) {
            this.pendingResult = goAsync();

            if (intent.getExtras().getString("broadcastType").equals("keyboardBroadcast")) {
                new CreateTraitDefinitionAsyncTask(this).execute();
            }
            else {
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
}
