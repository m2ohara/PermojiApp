package com.permoji.notifications;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 25/05/18.
 */

public class MockNotificationRepository {

    private Context context;
    private LiveData<List<Notification>> notifications;

    public MockNotificationRepository(Context context) {
        this.context = context;
        notifications = generateLiveNotificationList();
    }

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    private LiveData<List<Notification>> generateLiveNotificationList() {

        final List<Notification> notificationList = generateNotificationsList();

        LiveData liveNotifications = new LiveData<Notification>() {
            @Override
            protected void setValue(Notification value) {
                super.setValue(value);

                for(Notification notification : notificationList) {
                    super.setValue(notification);
                }

            }
        };

        return liveNotifications;
    }

    public List<Notification> generateNotificationsList() {
        Notification notification1 = new Notification();
        notification1.setNotifierImage(context.getDrawable(context.getResources().getIdentifier("contactimage1", "drawable", context.getPackageName())));
        notification1.setDetail("influenced by Cheerful");
        notification1.setTimePassed("1 hour ago");
        notification1.setTraitCodepoint(0x1F601);
        notification1.setId(1);

        Notification notification2 = new Notification();
        notification2.setNotifierImage(context.getDrawable(context.getResources().getIdentifier("contactimage2", "drawable", context.getPackageName())));
        notification2.setDetail("influenced by Joker");
        notification2.setTimePassed("2 hours ago");
        notification2.setTraitCodepoint(0x1F602);
        notification2.setId(2);

        Notification notification3 = new Notification();
        notification3.setNotifierImage(context.getDrawable(context.getResources().getIdentifier("contactimage3", "drawable", context.getPackageName())));
        notification3.setDetail("influenced by Cheerful");
        notification3.setTimePassed("1 hour ago");
        notification3.setTraitCodepoint(0x1F601);
        notification3.setId(3);

        final List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(notification1);
        notifications.add(notification2);
        notifications.add(notification3);

        return  notifications;
    }

}
