package com.permoji.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.database.BaseDAO;
import com.permoji.model.entity.User;

import java.util.List;

/**
 * Created by michael on 01/10/18.
 */

@Dao
public interface UserDao extends BaseDAO<User> {

    @Query("Select * from user")
    LiveData<List<User>> get();
}
