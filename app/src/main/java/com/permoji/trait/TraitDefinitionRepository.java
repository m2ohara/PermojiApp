package com.permoji.trait;

import android.content.Context;

import com.permoji.cache.LocalDatabase;

import java.util.List;

/**
 * Created by michael on 11/06/18.
 */

public class TraitDefinitionRepository {

    //TODO: Separate into individual repos based on tables
    LocalDatabase localDatabase;

    public TraitDefinitionRepository(Context context) {
        localDatabase = LocalDatabase.getInstance(context);
    }

    public List<TraitDefinition> getLatestTraitDefinitionsByAmount(int amount) {
        return null;
    }

    public void insert(TraitDefinition traitDefinition) {

    }

    public void update(TraitDefinition traitDefinition) {

    }

    public boolean isTraitDefinitionTablesEmpty() {
        return false;
    }

    public List<TraitStatement> getTraitStatementsByCodepoint(int codepoint) {
        return null;
    }


    public List<TraitFiller> getTraitFillersByCodepoint(int codepoint) {
        return null;
    }


    public List<Notifier> getAllNotifiers() {
        return null;
    }

    public void insertNotifier(Notifier notifier) {

    }

    public void insertNotifierFiller(TraitNotifierFiller traitNotifierFiller) {

    }

}
