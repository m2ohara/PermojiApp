package com.permoji.cache;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.permoji.api.trait.Trait;

import java.util.List;

/**
 * Created by michael on 24/05/18.
 */

@Dao
public interface TraitDao extends BaseDAO<Trait> {

    @Query("Select * from Trait")
    LiveData<List<Trait>> getAllLive();

    @Query("Select * from Trait")
    List<Trait> getAll();

    @Insert
    void insert(Trait trait);

    @Update
    void update(Trait trait);

    @Query("Delete from Trait")
    void deleteAll();

}
