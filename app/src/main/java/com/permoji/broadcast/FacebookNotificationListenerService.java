package com.permoji.broadcast;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.vdurmont.emoji.EmojiParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
    private String previousKey = "";
//    private NotificationReceiver notificationReceiver;
    private static String NOTIFICATION = "io.github.ctrlaltdel.aosp.ime";

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG, "KEY :"+sbn.getKey() + " ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        String filePath  = "";
        String name = "";
        ArrayList<Integer> emojiCodepoints = new ArrayList<>();

        if(!(sbn.getPackageName().contains("whatsapp") || sbn.getPackageName().contains("facebook.orca"))) {
            return;
        }

        previousKey = sbn.getKey();
        String androidText = null;
        Bundle extras = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            extras = sbn.getNotification().extras;
            Log.d(TAG, extras.toString());

            try {
                androidText = "" + extras.getString("android.text"); //Concatenation needed for spannable strings

                //Group message whatsapp: Bundle[{android.title=Leanna @ Boys and Girls Social, android.subText=null,
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }


        //Store notification icon if exists
        if(extras.get(Notification.EXTRA_LARGE_ICON) != null) {

            Bitmap bigIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);

            name = "User";
            if(androidText != null) {
                name = "" + extras.getString("android.title");
            }

            filePath = writeImageToCache(bigIcon, name);
        }



        if(androidText != null) {

            String text = null;

            text = "" + extras.getString("android.text");

            List<String> emojis = EmojiParser.extractEmojis(text);

            if(emojis == null || emojis.size() == 0) {
                return;
            }

            for(String emoji : emojis) {

                int codepoint = 0;

                codepoint = emoji.codePointAt(0);

                emojiCodepoints.add(codepoint);

                Log.d(TAG, "Emoji recorded : "+Long.toHexString(emoji.codePointAt(0)));
            }
        }


        if(emojiCodepoints.size() > 0) {
            broadcastNotification(filePath, name, emojiCodepoints);
        }
    }

    private String writeImageToCache(Bitmap bigIcon, String name) {

        File outFile = null;
        String externalDirectory = "Permoji";
        OutputStream stream = null;

        try {

            File dir = new File(Environment.getExternalStorageDirectory(), externalDirectory);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            outFile = new File(Environment.getExternalStorageDirectory() + "/" + externalDirectory + "/",
                    name + ".png");
            if (outFile.exists()) {
                outFile.delete();
            }

            stream = new FileOutputStream(outFile);

            bigIcon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
        }
        catch (Exception e) {
            Log.e(TAG,  e.getMessage());
            return null;
        }

        return outFile.getPath();
    }

    private void broadcastNotification(String filePath, String name, ArrayList<Integer> emojiCodepoints) {
        Intent intent = new Intent(NOTIFICATION);
        Bundle extra = new Bundle();
        extra.putIntegerArrayList("emojiCodepoints", emojiCodepoints);
        extra.putString("filePath", filePath);
        extra.putString("name",name);
        extra.putString("broadcastType", "notificationBroadcast");
        intent.putExtras(extra);
        Log.i(TAG,"Broadcasting intent " + intent.getAction() + " for notification " + name);

        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

}
