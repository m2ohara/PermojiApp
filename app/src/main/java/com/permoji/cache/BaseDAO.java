package com.permoji.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.permoji.api.trait.Trait;

import java.util.List;

/**
 * Created by michael on 24/05/18.
 */

@Dao
public interface BaseDAO<T> {

    @Insert
    void insert(T object);

    @Update
    void update(T object);

    @Delete
    void delete(T object);


}
