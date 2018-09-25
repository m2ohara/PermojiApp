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
    private LiveData<List<UserNotification>> notifications;

    public MockNotificationRepository(Context context) {
        this.context = context;
        notifications = generateLiveNotificationList();
    }

    public LiveData<List<UserNotification>> getNotifications() {
        return notifications;
    }

    private LiveData<List<UserNotification>> generateLiveNotificationList() {

        final List<UserNotification> userNotificationList = generateNotificationsList();

        LiveData liveNotifications = new LiveData<UserNotification>() {
            @Override
            protected void setValue(UserNotification value) {
                super.setValue(value);

                for(UserNotification notification : userNotificationList) {
                    super.setValue(notification);
                }

            }
        };

        return liveNotifications;
    }

    public List<UserNotification> generateNotificationsList() {
        UserNotification userNotification1 = new UserNotification();
//        userNotification1.setNotifierImagePath(context.getDrawable(context.getResources().getIdentifier("contactimage1", "drawable", context.getPackageName())));
        userNotification1.setDetail("influenced by Cheerful");
        userNotification1.setTimeStamp("1 hour ago");
        userNotification1.setTraitCodepoint(0x1F601);
        userNotification1.setId(1);

        UserNotification userNotification2 = new UserNotification();
//        userNotification2.setNotifierImagePath(context.getDrawable(context.getResources().getIdentifier("contactimage2", "drawable", context.getPackageName())));
        userNotification2.setDetail("influenced by Joker");
        userNotification2.setTimeStamp("2 hours ago");
        userNotification2.setTraitCodepoint(0x1F602);
        userNotification2.setId(2);

        UserNotification userNotification3 = new UserNotification();
//        userNotification3.setNotifierImagePath(context.getDrawable(context.getResources().getIdentifier("contactimage3", "drawable", context.getPackageName())));
        userNotification3.setDetail("influenced by Cheerful");
        userNotification3.setTimeStamp("1 hour ago");
        userNotification3.setTraitCodepoint(0x1F601);
        userNotification3.setId(3);

        final List<UserNotification> userNotifications = new ArrayList<UserNotification>();
        userNotifications.add(userNotification1);
        userNotifications.add(userNotification2);
        userNotifications.add(userNotification3);

        return userNotifications;
    }

}
