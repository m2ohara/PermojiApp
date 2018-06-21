package com.permoji.cache.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.permoji.cache.BaseDAO;
import com.permoji.model.TraitDefinition;
import com.permoji.model.result.TraitResult;

import java.util.List;

/**
 * Created by michael on 11/06/18.
 */

@Dao
public interface TraitDefinitionDao extends BaseDAO<TraitDefinition> {

    @Query("Select * from trait_definition order by datecreated desc LIMIT :count")
    List<TraitDefinition> getLatestByCount(int count);

    @Query("Select * from trait_definition") // as td " +
            //"join trait_statement as ts on td.statementId = ts.id " +
            //"join trait_notifier_filler as tnf on td.id = tnf.traitDefinitionId")
    LiveData<List<TraitResult>> getAllTraitViewModels();
}
