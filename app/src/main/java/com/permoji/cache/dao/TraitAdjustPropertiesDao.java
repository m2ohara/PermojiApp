package com.permoji.cache.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.cache.BaseDAO;
import com.permoji.model.entity.TraitAdjustProperties;

import java.util.List;

/**
 * Created by michael on 21/06/18.
 */
@Dao
public interface TraitAdjustPropertiesDao extends BaseDAO<TraitAdjustProperties> {

    @Query("Select * from trait_adjust_properties")
    public LiveData<List<TraitAdjustProperties>> getLiveTraitAdjustProperties();

    @Query("Select * from trait_adjust_properties")
    public List<TraitAdjustProperties> getTraitAdjustProperties();

}
