package com.permoji.cache;

import android.arch.persistence.room.Query;

import com.permoji.trait.TraitDefinition;

import java.util.List;

/**
 * Created by michael on 11/06/18.
 */

public interface TraitDefinitionDao extends BaseDAO<TraitDefinition> {

    @Query("Select * from traitdefinition where id = :id")
    List<TraitDefinition> getTraitDefinitionById(int id);
}
