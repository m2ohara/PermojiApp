package com.permoji.cache;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.api.trait.Trait;

import java.util.List;

/**
 * Created by michael on 24/05/18.
 */

@Dao
public interface TraitDao extends BaseDAO<Trait> {

    @Query("Select * from trait")
    LiveData<List<Trait>> getAll();

    @Query("Delete from trait")
    void deleteAll();

}
