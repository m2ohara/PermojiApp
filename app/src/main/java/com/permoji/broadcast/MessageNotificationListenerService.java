package com.permoji.broadcast;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 18/09/18.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MessageNotificationListenerService extends NotificationListenerService {


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(this.getClass().getSimpleName(), "**********  onNotificationPosted");
        Log.i(this.getClass().getSimpleName(), " ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());


        NotificationExtraction notificationExtraction = null;
        if(sbn.getPackageName().contains("whatsapp") || sbn.getPackageName().contains("facebook.orca")) {
            notificationExtraction = NotificationsExtractor.get().extract(sbn);

            if(notificationExtraction != null) {
                ArrayList<Integer> emojiCodepoints = extractEmojis(notificationExtraction.getNotifierTextMessage());
                if(emojiCodepoints.size() == 0) {
                    Log.i(this.getClass().getSimpleName(), "No extracted emojis");
                    return;
                }
                else {
                    NotificationsExtractor.get().updateProcessedMessage(notificationExtraction);
                    broadcastNotification(notificationExtraction.getNotifierImagePath(), notificationExtraction.getNotifierName(), emojiCodepoints);
                }
            }
        }


    }

    private ArrayList<Integer> extractEmojis(String text) {

        ArrayList<Integer> emojiCodepoints = new ArrayList<>();

        List<String> emojis = EmojiParser.extractEmojis(text);

        if(emojis == null || emojis.size() == 0) {
            return emojiCodepoints;
        }

        for(String emoji : emojis) {

            int codepoint = 0;

            codepoint = emoji.codePointAt(0);

            emojiCodepoints.add(codepoint);

            Log.d(this.getClass().getSimpleName(), "Emoji recorded : "+Long.toHexString(emoji.codePointAt(0)));
        }

        return emojiCodepoints;
    }

    private void broadcastNotification(String filePath, String name, ArrayList<Integer> emojiCodepoints) {
        Intent intent = new Intent(this.getBaseContext(), NotificationReceiver.class);
        Bundle extra = new Bundle();
        extra.putIntegerArrayList("emojiCodepoints", emojiCodepoints);
        extra.putString("filePath", filePath);
        extra.putString("name",name);
        extra.putString("broadcastType", "notificationBroadcast");
        intent.putExtras(extra);
        Log.i(this.getClass().getSimpleName(),"Broadcasting intent for notification " + name);

        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

}
