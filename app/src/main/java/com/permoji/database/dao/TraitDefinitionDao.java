package com.permoji.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.database.BaseDAO;
import com.permoji.model.entity.Trait;
import com.permoji.model.result.TraitResult;

import java.util.List;

/**
 * Created by michael on 11/06/18.
 */

@Dao
public interface TraitDefinitionDao extends BaseDAO<Trait> {

    @Query("Select * from trait_definition order by datecreated desc LIMIT :count")
    List<TraitResult> getLatestByCount(int count);

    @Query("Select * from trait_definition")
    LiveData<List<TraitResult>> getAllLive();

    @Query("Select * from trait_definition")
    List<TraitResult> getAll();

    @Query("Select * from trait_definition")
    List<Trait> getAllTraits();

    @Query("Select * from trait_definition order by datecreated desc LIMIT :count")
    List<Trait> getLatestTraitsByCount(int count);
}


