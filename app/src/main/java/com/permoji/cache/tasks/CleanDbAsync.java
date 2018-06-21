package com.permoji.cache.tasks;

import android.os.AsyncTask;

import com.permoji.cache.LocalDatabase;
import com.permoji.cache.dao.TraitDao;
import com.permoji.cache.dao.UserNotificationDao;

/**
 * Created by michael on 25/05/18.
 */

public class CleanDbAsync extends AsyncTask<Void, Void, Void> {

    private TraitDao traitDao;
    private UserNotificationDao userNotificationDao;

    public CleanDbAsync(LocalDatabase db) {
        this.traitDao = db.traitDao();
        this.userNotificationDao = db.userNotificationDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        traitDao.deleteAll();

        userNotificationDao.deleteAll();

        return null;
    }
}
