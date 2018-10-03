package com.permoji.broadcast;

import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.permoji.helper.FileWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.RegEx;

/**
 * Created by michael on 18/09/18.
 */

public class WhatsappExtractor {

    private static WhatsappExtractor instance;
    private static List<NotificationExtraction> processedMessages = new ArrayList<>();
    private int processedMessagesLimit = 100;

    private String previousProcessedMessage;
    private String TITLE = "android.title";
    private String MESSAGE = "android.text";

    private StatusBarNotification sbn = null;
    private String message = null;
    private String title = null;

    private WhatsappExtractor() { }

    public static WhatsappExtractor get() {
        if(instance == null) {
            instance = new WhatsappExtractor();
        }
        return instance;
    }

    public NotificationExtraction extract(StatusBarNotification sbn) {

        this.sbn = sbn;
        this.message = getStringValue(MESSAGE);
        this.title = getStringValue(TITLE);
        NotificationExtraction result = new NotificationExtraction();

        if(message == null || title == null) {
            return null;
        }

        title = cleanLastCharacterStringValue(title);

        if(isStatusNotification()) {
            return null;
        }

        if(isGroupChat()) {
            Log.i(this.getClass().getSimpleName(), "Ignoring group message");
            //TODO: populate from group chat
            return null;
        }
        else {
            result = populateValuesFromSingleChat(result, title, message);
        }

        return result;

    }

    public void updateProcessedMessage(NotificationExtraction notificationExtraction) {

        if(processedMessages.size() == processedMessagesLimit) {
            processedMessages.remove(0);
        }

        processedMessages.add( notificationExtraction);
    }

    private boolean isDuplicate(String name, String message) {
        for(NotificationExtraction notificationExtraction : processedMessages) {
            if (message.equals(notificationExtraction.getNotifierTextMessage()) && name.equals(notificationExtraction.getNotifierName())) {
                Log.i(this.getClass().getSimpleName(), "Ignoring duplicate");
                return true;
            }
        }
        return false;
    }

    private boolean isStatusNotification() {
        if(message.matches("([0-9]* [new ]?message[s]?)") ) {
            Log.i(this.getClass().getSimpleName(), "Ignoring status notification");
            return true;
        }
        return false;
    }

    private boolean isMSISDN(String value) {
        if(value.replaceAll("([+][0-9 ]{0,15})", "").isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean isGroupChat() {
        if(title.indexOf(':') > 0 ||  message.indexOf(':') > 0 || title.indexOf('@') > 0) {
            return true;
        }

        return false;
    }

    private String getStringValue(String stringValue) {
        String androidText = null;
        Bundle extras = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            extras = sbn.getNotification().extras;
            Log.d(this.getClass().getSimpleName(), extras.toString());

            try {
                androidText = "" + extras.getString(stringValue); //Concatenation needed for spannable strings
            }
            catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
        }

        return  androidText;
    }

    private String cleanStringValue(String value, String regEx) {
        return value.replaceAll(regEx, "");
    }

    private String cleanLastCharacterStringValue(String value) {
//        if(value.endsWith(":")) {
//            value = value.substring(0, value.length()-3);
//        }

        value = value.replaceAll(": \u200B$", "");

        return value;
    }

    private String getImageFilePath(String name) throws Exception {

        String filePath = null;
        Bundle extras = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            extras = sbn.getNotification().extras;
            Log.d(this.getClass().getSimpleName(), extras.toString());

            //Store notification icon if exists
            if(extras.get(Notification.EXTRA_LARGE_ICON) != null) {

                Bitmap bigIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);

                filePath = FileWriter.writeImageToLocalDirectory(bigIcon, name);
            }

        }

        return filePath;

    }

//    private String writeImageToLocalDirectory(Bitmap bigIcon, String name) throws Exception {
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
//            Log.e(this.getClass().getSimpleName(),  e.getMessage());
//            throw e;
//        }
//        catch (Exception e) {
//            Log.e(this.getClass().getSimpleName(),  e.getMessage());
//            throw e;
//        }
//
//        return outFile.getPath();
//    }

    private NotificationExtraction populateValuesFromSingleChat(NotificationExtraction notificationExtraction, String name, String message) {

        if(isMSISDN(name)) {
            Log.i(this.getClass().getSimpleName(), "Ignoring MSISDN notifier");
            return null;
        }

        if(isDuplicate(name, message)) {
            Log.i(this.getClass().getSimpleName(), "Ignoring single chat duplicate");
            return null;
        }

        name = cleanStringValue(name, "( [(][0-9]* [new ]?message[s]?[)])");

        String imageFilePath = "";
        try {
            imageFilePath = getImageFilePath(name);
        }catch (Exception e) {
            return  null;
        }

        notificationExtraction.setNotifierName(name);
        notificationExtraction.setNotifierTextMessage(message);
        notificationExtraction.setNotifierImagePath(imageFilePath);
        notificationExtraction.setTimeStamp(new Date());

        return notificationExtraction;
    }

    private String getNameFromGroup(String androidTitle, boolean hasTextLines) {
        String name = null;
        //If in Group chat
        if (androidTitle.contains("@")) {

            if(hasTextLines) {
                return null;
            }

            name = name.substring(0, name.indexOf("@"));

            //Eliminate numbers
            if (name.contains("+") || name.replaceAll("[0-9 ]", "").contains("")) {
                return null;
            }
        }
        return name;
    }
}

class NotificationExtraction {

    private String notifierImagePath;
    private String notifierTextMessage;
    private String notifierName;
    private Date timeStamp;

    public String getNotifierImagePath() {
        return notifierImagePath;
    }

    public void setNotifierImagePath(String notifierImagePath) {
        this.notifierImagePath = notifierImagePath;
    }

    public String getNotifierTextMessage() {
        return notifierTextMessage;
    }

    public void setNotifierTextMessage(String notifierTextMessage) {
        this.notifierTextMessage = notifierTextMessage;
    }

    public String getNotifierName() {
        return notifierName;
    }

    public void setNotifierName(String notifierName) {
        this.notifierName = notifierName;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
