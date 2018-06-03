package com.permoji.notifications;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by michael on 25/05/18.
 */

public class NotificationViewModel extends AndroidViewModel {

    private UserNotificationRepository userNotificationRepository;
    private LiveData<List<UserNotification>> notifications;

    public NotificationViewModel(@NonNull Application application) {
        super(application);

        userNotificationRepository = new UserNotificationRepository(application);
        notifications = userNotificationRepository.getLiveNotificationsOrdered();
    }

    public LiveData<List<UserNotification>> getNotifications() {
        return notifications;
    }

}
