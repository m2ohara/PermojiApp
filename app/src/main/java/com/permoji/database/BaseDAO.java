package com.permoji.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

/**
 * Created by michael on 24/05/18.
 */

@Dao
public interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T object);

    @Update
    void update(T object);

    @Delete
    void delete(T object);


}
