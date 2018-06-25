package com.permoji.repository;

import android.content.Context;

import com.permoji.cache.LocalDatabase;
import com.permoji.cache.dao.NotifierFillerDao;
import com.permoji.model.entity.NotifierFiller;
import com.permoji.model.result.NotifierFillerResult;

/**
 * Created by michael on 22/06/18.
 */

public class NotifierFillerRepository {

    private NotifierFillerDao notifierFillerDao;

    public NotifierFillerRepository(Context context) {
        LocalDatabase db = LocalDatabase.getInstance(context);
        notifierFillerDao = db.notifierFillerDao();
    }

    public int insert(NotifierFiller notifierFiller) {
        return (int) notifierFillerDao.insert(notifierFiller);
    }

    public void update(NotifierFiller notifierFiller) {
        notifierFillerDao.update(notifierFiller);
    }

    public void delete(NotifierFiller notifierFiller) {
        notifierFillerDao.delete(notifierFiller);
    }
}
