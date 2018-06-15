package com.permoji.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.permoji.trait.data.TraitStatement;

import java.util.List;

/**
 * Created by michael on 12/06/18.
 */
@Dao
public interface TraitStatementDao extends BaseDAO<TraitStatement> {

    @Query("Select * from trait_statement")
    List<TraitStatement> getAll();

    @Query("Select * from trait_statement where codepoint = :codepoint")
    List<TraitStatement> getByCodepoint(String codepoint);

    @Query("Select count(*) from trait_statement")
    int getTraitStatementCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertAll(List<TraitStatement> traitStatements);
}
