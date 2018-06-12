package com.permoji.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.permoji.api.trait.Trait;
import com.permoji.trait.TraitFiller;
import com.permoji.trait.TraitStatement;

import java.util.List;

/**
 * Created by michael on 12/06/18.
 */
@Dao
public interface TraitFillerDao extends BaseDAO<Trait> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertAll(List<TraitFiller> traitFillerList);
}
