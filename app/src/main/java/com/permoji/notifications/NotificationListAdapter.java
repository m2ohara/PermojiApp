package com.permoji.notifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.permoji.compatability.EmojiInitCallback;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 26/05/18.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationsViewHolder> {

    class NotificationsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView notifierImage;
        private final TextView detail;
        private final TextView time;

        private NotificationsViewHolder(View itemView) {
            super(itemView);

            notifierImage = itemView.findViewById(R.id.user_notifier_image);
            detail = itemView.findViewById(R.id.user_notification_detail);
            time = itemView.findViewById(R.id.user_notification_time);

        }
    }

    private String TAG = this.getClass().getSimpleName();
    LayoutInflater inflater;
    List<UserNotification> userNotifications;

    public NotificationListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }


    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.user_notification_list, parent, false);
        return new NotificationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {

        UserNotification userNotification = userNotifications.get(position);

        String notification = userNotification.getDetail() + " " + userNotification.getTraitName() + " " + new String(Character.toChars(userNotification.getTraitCodepoint()));

        File imageFile = new File(userNotification.getImagePath());
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        holder.notifierImage.setImageBitmap(imageBitmap);
        EmojiCompat.get().registerInitCallback(new EmojiInitCallback(holder.detail, notification));

        String timePassed = getHoursPassed(userNotification);
        timePassed = timePassed.equals("0") ? "     Now" : timePassed.equals("1") ? timePassed + " hour ago" : timePassed + " hours ago";
        holder.time.setText(timePassed);

    }

    @Override
    public int getItemCount() {
        if(userNotifications != null) {
            return  userNotifications.size();
        }
        return 0;
    }

    public void setUserNotifications(List<UserNotification> userNotifications) {
        this.userNotifications = userNotifications;
        notifyDataSetChanged();
    }

    private String getHoursPassed(UserNotification userNotification) {
        Date notificationTime = null;
        try {
            notificationTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(userNotification.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = new Date().getTime() - notificationTime.getTime();
        long timepassed = TimeUnit.MILLISECONDS.toHours(difference);

        return  Integer.toString((int)timepassed);
    }
}
