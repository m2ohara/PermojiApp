package com.permoji.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.permoji.database.BaseDAO;
import com.permoji.model.entity.NotifierFiller;

import java.util.List;

/**
 * Created by michael on 20/06/18.
 */
@Dao
public interface NotifierFillerDao extends BaseDAO<NotifierFiller> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NotifierFiller> notifierFillers);
}
