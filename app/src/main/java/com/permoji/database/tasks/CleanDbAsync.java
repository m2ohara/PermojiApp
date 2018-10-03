package com.permoji.database.tasks;

import android.os.AsyncTask;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.UserNotificationDao;

/**
 * Created by michael on 25/05/18.
 */

public class CleanDbAsync extends AsyncTask<Void, Void, Void> {

    //TODO: Delete traits
    private UserNotificationDao userNotificationDao;

    public CleanDbAsync(LocalDatabase db) {
        this.userNotificationDao = db.userNotificationDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        userNotificationDao.deleteAll();

        return null;
    }
}
