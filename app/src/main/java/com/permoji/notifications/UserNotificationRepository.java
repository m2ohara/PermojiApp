package com.permoji.notifications;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.permoji.cache.LocalDatabase;
import com.permoji.cache.UserNotificationDao;

import java.util.List;

/**
 * Created by michael on 01/06/18.
 */

public class UserNotificationRepository {

    private UserNotificationDao userNotificationDao;
    private LiveData<List<UserNotification>> liveNotificationsOrdered;

    public UserNotificationRepository(Context context) {
        LocalDatabase db = LocalDatabase.getInstance(context);
        userNotificationDao = db.userNotificationDao();
        liveNotificationsOrdered = userNotificationDao.getAllOrderedLive();
    }

    public LiveData<List<UserNotification>> getLiveNotificationsOrdered() {
        return liveNotificationsOrdered;
    }

    public List<UserNotification> getNotifications() {
        return userNotificationDao.getAll();
    }

    public void insertUserNotificationAsync(UserNotification notification) {
        new InsertUserNotificationAsync(userNotificationDao).execute(notification);
    }

    public void updateUserNotificationAsync(UserNotification notification) {
        new UpdateUserNotificationAsync(userNotificationDao).equals(notification);
    }

    private static class InsertUserNotificationAsync extends AsyncTask<UserNotification, Void, Void> {

        private  UserNotificationDao userNotificationDao;
        public InsertUserNotificationAsync(UserNotificationDao userNotificationDao) {this.userNotificationDao = userNotificationDao;}

        @Override
        protected Void doInBackground(UserNotification... userNotifications) {
            userNotificationDao.insert(userNotifications[0]);
            return null;
        }
    }

    private static class UpdateUserNotificationAsync extends AsyncTask<UserNotification, Void, Void> {

        private  UserNotificationDao userNotificationDao;
        public UpdateUserNotificationAsync(UserNotificationDao userNotificationDao) {this.userNotificationDao = userNotificationDao;}

        @Override
        protected Void doInBackground(UserNotification... userNotifications) {
            userNotificationDao.update(userNotifications[0]);
            return null;
        }
    }

}
