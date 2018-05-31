package com.permoji.broadcast;

import android.annotation.SuppressLint;
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
//    private NotificationReceiver notificationReceiver;
    private static String NOTIFICATION = "io.github.ctrlaltdel.aosp.ime";

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        String filePath  = "";
        String name = "";
        ArrayList<Integer> emojiCodepoints = new ArrayList<>();

        if(!(sbn.getPackageName().contains("whatsapp") || sbn.getPackageName().contains("facebook.orca"))) {
            return;
        }

        String androidText = null;
        Bundle extras = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            extras = sbn.getNotification().extras;
            Log.d(TAG, extras.toString());

            try {
                androidText = extras.getString("android.text");
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }



        if(sbn.getNotification().icon != 0) {

            Context context = null;
            try {
                context = createPackageContext(sbn.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Drawable drawable = context.getResources().getDrawable(sbn.getNotification().icon);

            name = "User";
            if(androidText != null) {

                if(sbn.getPackageName().contains("facebook.orca")) {
                    name = "" + extras.getString("android.title");
                }
                if(sbn.getPackageName().contains("whatsapp")) {
                    name = "" + extras.getString("android.text");
                    name = name.substring(0, name.indexOf(':'));
                }
            }

            filePath = writeImageToCache(drawable, name);
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

    private String writeImageToCache(Drawable drawable, String name) {

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        File outFile = null;
        OutputStream stream = null;

        try {

            outFile = new File(Environment.getExternalStorageDirectory(),
                    name + ".png");
            if (outFile.exists()) {
                outFile.delete();
            }

            stream = new FileOutputStream(outFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
        intent.putExtras(extra);
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
//
//
//
//
//    class NotificationReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//        }
//    }
}
