package com.permoji.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.database.BaseDAO;
import com.permoji.model.entity.Notifier;

import java.util.List;

/**
 * Created by michael on 13/06/18.
 */
@Dao
public interface NotifierDao extends BaseDAO<Notifier> {

    @Query("Select * from trait_notifier")
    List<Notifier> getAll();
}
