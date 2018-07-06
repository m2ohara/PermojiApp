package com.permoji.cache.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.cache.BaseDAO;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.result.TraitNotifierFillerResult;

import java.util.List;

/**
 * Created by michael on 14/06/18.
 */
@Dao
public interface TraitNotifierFillerDao extends BaseDAO<TraitNotifierFiller> {

    @Query("Select * from trait_notifier_filler where traitDefinitionId = :id")
    public LiveData<List<TraitNotifierFillerResult>> getLiveDataById(int id);

}
