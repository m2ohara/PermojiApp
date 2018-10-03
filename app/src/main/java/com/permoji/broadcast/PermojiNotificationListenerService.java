//package com.permoji.broadcast;
//
//import android.app.Notification;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.service.notification.NotificationListenerService;
//import android.service.notification.StatusBarNotification;
//import android.support.annotation.RequiresApi;
//import android.util.Log;
//
//import com.vdurmont.emoji.EmojiParser;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by michael on 30/05/18.
// */
////TODO: Encapsulate logic, implement strategy pattern
//@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//public class PermojiNotificationListenerService extends NotificationListenerService {
//
//    private String TAG = this.getClass().getSimpleName();
//    private BroadcastType broadcastType;
//    private static String NOTIFICATION = "io.github.ctrlaltdel.aosp.ime";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//
//    @Override
//    public void onNotificationPosted(StatusBarNotification sbn) {
//        Log.i(TAG,"**********  onNotificationPosted");
//        Log.i(TAG, "KEY :"+sbn.getKey() + " ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
//
//        String filePath  = "";
//        String name = "";
//        ArrayList<Integer> emojiCodepoints = new ArrayList<>();
//
//        if(!(sbn.getPackageName().contains("whatsapp") || sbn.getPackageName().contains("facebook.orca"))) {
//            return;
//        }
//
//        broadcastType = sbn.getPackageName().contains("whatsapp") ? BroadcastType.Whatsapp : BroadcastType.Facebook;
//
//        String androidText = null;
//        Bundle extras = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            extras = sbn.getNotification().extras;
//            Log.d(TAG, extras.toString());
//
//            try {
//                androidText = "" + extras.getString("android.text"); //Concatenation needed for spannable strings
//            }
//            catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//        }
//
//        name = getNameFromBroadcast(extras.getString("android.title"), broadcastType, extras.containsKey("android.textLines"));
//
//        if(name == null) {
//            return;
//        }
//
//        Log.e(TAG, "Valid user: "+name);
//
//        //Store notification icon if exists
//        if(extras.get(Notification.EXTRA_LARGE_ICON) != null) {
//
//            Bitmap bigIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
//
//            filePath = writeImageToLocalDirectory(bigIcon, name);
//        }
//
//
//
//        if(androidText != null) {
//
//            String text = null;
//
//            text = "" + extras.get("android.text");
//
//            List<String> emojis = EmojiParser.extractEmojis(text);
//
//            if(emojis == null || emojis.size() == 0) {
//                return;
//            }
//
//            for(String emoji : emojis) {
//
//                int codepoint = 0;
//
//                codepoint = emoji.codePointAt(0);
//
//                emojiCodepoints.add(codepoint);
//
//                Log.d(TAG, "Emoji recorded : "+Long.toHexString(emoji.codePointAt(0)));
//            }
//        }
//
//
//        if(emojiCodepoints.size() > 0) {
//            broadcastNotification(filePath, name, emojiCodepoints);
//        }
//    }
//
//    private String writeImageToLocalDirectory(Bitmap bigIcon, String name) {
//
//        File outFile = null;
//        String localDirectory = "/Permoji/";
//        OutputStream stream = null;
//
//        try {
//
//            File dir = new File(Environment.getExternalStorageDirectory(), localDirectory);
//            if(!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            outFile = new File(Environment.getExternalStorageDirectory() + localDirectory,
//                    name + ".png");
//            if (outFile.exists()) {
//                outFile.delete();
//            }
//
//            stream = new FileOutputStream(outFile);
//
//            bigIcon.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            stream.flush();
//            stream.close();
//        }
//        catch (SecurityException e) {
//            Log.e(TAG,  e.getMessage());
//            return null;
//        }
//        catch (Exception e) {
//            Log.e(TAG,  e.getMessage());
//            return null;
//        }
//
//        return outFile.getPath();
//    }
//
//    private String getNameFromTextBroadcast(String androidText, BroadcastType broadcastType) {
//        String name = "Notifier";
//        if(androidText != null) {
//            name = "" + androidText;
//
//            if(name.contains(":")) {
//                name = name.substring(0, name.indexOf(" :"));
//            }
//
//            name = name.replaceAll("[^a-zA-Z0-9 ]", "");;
//        }
//
//        return name;
//    }
//
//    private String getNameFromBroadcast(String androidTitle, BroadcastType broadcastType, boolean hasTextLines) {
//        String name = null;
//
//        if(androidTitle != null) {
//            name = "" + androidTitle;
//
//            //If in whatsapp group
//            if (broadcastType == BroadcastType.Whatsapp && androidTitle.contains("@")) {
//
//                if(hasTextLines) {
//                    return null;
//                }
//
//                name = name.substring(0, name.indexOf("@"));
//
//                if (name.contains("+") || name.replaceAll("[0-9 ]", "").contains("")) {
//                    return null;
//                }
//            }
//
//
//            if (name.contains("(")) {
//                name = name.substring(0, name.indexOf(" ("));
//            }
//
//            name = name.replaceAll("[^a-zA-Z0-9 ]", "");
//        }
//
//        return name;
//    }
//
//    private void broadcastNotification(String filePath, String name, ArrayList<Integer> emojiCodepoints) {
//        Intent intent = new Intent(this.getBaseContext(), NotificationReceiver.class);
//        Bundle extra = new Bundle();
//        extra.putIntegerArrayList("emojiCodepoints", emojiCodepoints);
//        extra.putString("filePath", filePath);
//        extra.putString("name",name);
//        extra.putString("broadcastType", "notificationBroadcast");
//        intent.putExtras(extra);
//        Log.i(TAG,"Broadcasting intent " + intent.getAction() + " for notification " + name);
//
//        sendBroadcast(intent);
//    }
//
//    @Override
//    public void onNotificationRemoved(StatusBarNotification sbn) {
//
//    }
//
//    enum BroadcastType { Whatsapp, Facebook }
//
//}
