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

    private MockNotificationRepository mockNotificationRepository;
    private LiveData<List<Notification>> notifications;

    public NotificationViewModel(@NonNull Application application) {
        super(application);

        mockNotificationRepository = new MockNotificationRepository(application);
        notifications = mockNotificationRepository.getNotifications();
    }

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

}
