package com.permoji.cache;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.permoji.notifications.UserNotification;

import java.util.List;

/**
 * Created by michael on 01/06/18.
 */

@Dao
public interface UserNotificationDao extends BaseDAO<UserNotification> {

    @Query("Select * from UserNotification ORDER BY timeStamp DESC")
    LiveData<List<UserNotification>> getAllOrderedLive();

    @Query("Select * from UserNotification")
    List<UserNotification> getAll();

    @Insert
    void insert(UserNotification userNotification);

    @Update
    void update(UserNotification userNotification);

    @Query("Delete from UserNotification")
    void deleteAll();
}
