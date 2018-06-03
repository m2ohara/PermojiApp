package com.permoji.cache;

import android.os.AsyncTask;

import com.permoji.api.trait.Trait;
import com.permoji.notifications.UserNotification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by michael on 25/05/18.
 */

public class CleanDbAsync extends AsyncTask<Void, Void, Void> {

    private TraitDao traitDao;
    private UserNotificationDao userNotificationDao;

    CleanDbAsync(LocalDatabase db) {
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
