package com.permoji.cache.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.permoji.cache.BaseDAO;
import com.permoji.model.entity.TraitFiller;

import java.util.List;

/**
 * Created by michael on 12/06/18.
 */
@Dao
public interface TraitFillerDao extends BaseDAO<TraitFiller> {

    @Query("Select * from trait_filler where popularityWeight < 5 and popularityWeight >= 1")
    List<TraitFiller> getByPopularityWeight();

    @Query("Select * from trait_filler where codepoint = :codepoint")
    List<TraitFiller> getByCodepoint(String codepoint);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertAll(List<TraitFiller> traitFillerList);
}
