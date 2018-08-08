package com.permoji.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.permoji.database.BaseDAO;
import com.permoji.model.entity.TraitFiller;

import java.util.List;

/**
 * Created by michael on 12/06/18.
 */
@Dao
public interface TraitFillerDao extends BaseDAO<TraitFiller> {


    @Query("Select * from trait_filler where id = :id")
    List<TraitFiller> getById(int id);

    @Query("Select * from trait_filler where codepoint = :codepoint")
    List<TraitFiller> getByCodepoint(String codepoint);

    @Query("Select * from trait_filler")
    List<TraitFiller> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertAll(List<TraitFiller> traitFillerList);
}
