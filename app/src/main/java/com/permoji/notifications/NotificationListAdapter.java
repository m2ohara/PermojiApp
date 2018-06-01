package com.permoji.notifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

    LayoutInflater inflater;
    List<Notification> notifications;

    public NotificationListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.user_notification_list, parent, false);
        return new NotificationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {

        Notification notification = notifications.get(position);

        holder.notifierImage.setImageDrawable(notification.getNotifierImage());
        holder.detail.setText(notification.getDetail() + " " + new String(Character.toChars(notification.getTraitCodepoint())));
        holder.time.setText(notification.getTimePassed());

    }

    @Override
    public int getItemCount() {
        if(notifications != null) {
            return  notifications.size();
        }
        return 0;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }
}
