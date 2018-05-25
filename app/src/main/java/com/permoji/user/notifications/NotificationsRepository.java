package com.permoji.user.notifications;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 25/05/18.
 */

public class NotificationsRepository {

    private Context context;
    private LiveData<List<Notification>> notifications;

    public NotificationsRepository(Context context) {
        this.context = context;
        notifications = generateDummyNotifications();
    }

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    private LiveData<List<Notification>> generateDummyNotifications() {

        Notification notification1 = new Notification();
        notification1.setContactImagePath(context.getDir("Images", Context.MODE_PRIVATE)+"contactImage1");
        notification1.setDetail("vouched for Cheerful");
        notification1.setTime("1 hour ago");
        notification1.setTraitCodepoint(0x6F002);
        notification1.setId(1);

        Notification notification2 = new Notification();
        notification2.setContactImagePath(context.getDir("Images", Context.MODE_PRIVATE)+"contactImage2");
        notification2.setDetail("vouched for Joker");
        notification2.setTime("2 hours ago");
        notification2.setTraitCodepoint(0x6F001);
        notification2.setId(2);

        Notification notification3 = new Notification();
        notification3.setContactImagePath(context.getDir("Images", Context.MODE_PRIVATE)+"contactImage3");
        notification3.setDetail("vouched for Cheerful");
        notification3.setTime("1 hour ago");
        notification3.setTraitCodepoint(0x6F002);
        notification3.setId(3);

        final List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(notification1);
        notifications.add(notification2);
        notifications.add(notification3);

        LiveData liveNotifications = new LiveData<Notification>() {
            @Override
            protected void setValue(Notification value) {
                super.setValue(value);

                for(Notification notification : notifications) {
                    super.setValue(notification);
                }

            }
        };
        return liveNotifications;
    }

}
