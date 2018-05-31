package com.permoji.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;

import com.vdurmont.emoji.EmojiParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 30/05/18.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FacebookNotificationListenerService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NotificationReceiver notificationReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter("com.permoji.broadcast.FACEBOOK_NOTIFICATION_LISTENER_SERVICE");
        registerReceiver(notificationReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        String characters = (String) sbn.getNotification().tickerText;

        Bundle extras = sbn.getNotification().extras;
        Log.d(TAG, extras.toString());

        String text = null;

        if(extras.getString("android.text") != null) {

            text = "" + extras.getString("android.text");

            List<String> emojis = EmojiParser.extractEmojis(text);

            if(emojis == null || emojis.size() == 0) {
                return;
            }

            for(String emoji : emojis) {

                int codepoint = 0;

                codepoint = emoji.codePointAt(0);

                Log.d(TAG, "Emoji recorded : "+Long.toHexString(emoji.codePointAt(0)));
            }
        }


        Intent i = new  Intent("com.permoji.broadcast.FACEBOOK_NOTIFICATION_LISTENER_SERVICE");
        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }




    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
