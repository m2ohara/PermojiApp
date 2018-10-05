package com.permoji.repository;

import android.content.Context;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.NotifierFillerDao;
import com.permoji.model.entity.NotifierFiller;

import java.util.List;

/**
 * Created by michael on 22/06/18.
 */

public class NotifierFillerRepository {

    private NotifierFillerDao notifierFillerDao;

    public NotifierFillerRepository(Context context) {
        LocalDatabase db = LocalDatabase.getInstance(context);
        notifierFillerDao = db.notifierFillerDao();
    }

    public void insertAll(List<NotifierFiller> notifierFillers) {
        notifierFillerDao.insertAll(notifierFillers);
    }

    public void update(NotifierFiller notifierFiller) {
        notifierFillerDao.update(notifierFiller);
    }

    public void delete(NotifierFiller notifierFiller) {
        notifierFillerDao.delete(notifierFiller);
    }
}
