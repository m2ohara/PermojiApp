package com.permoji.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.trait.data.TraitDefinition;

import java.util.List;

/**
 * Created by michael on 11/06/18.
 */

@Dao
public interface TraitDefinitionDao extends BaseDAO<TraitDefinition> {

    @Query("Select * from trait_definition order by datecreated desc LIMIT :count")
    List<TraitDefinition> getLatestByCount(int count);
}
