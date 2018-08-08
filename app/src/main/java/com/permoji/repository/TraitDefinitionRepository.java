package com.permoji.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.NotifierDao;
import com.permoji.database.dao.NotifierFillerDao;
import com.permoji.database.dao.TraitDefinitionDao;
import com.permoji.model.entity.Notifier;
import com.permoji.model.entity.NotifierFiller;
import com.permoji.model.entity.Trait;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;

import java.util.List;

/**
 * Created by michael on 11/06/18.
 */

public class TraitDefinitionRepository {

    //TODO: Separate into individual repos based on tables
    private LocalDatabase localDatabase;
    private TraitDefinitionDao traitDefinitionDao;
//    private TraitFillerDao traitFillerDao;
//    private NotifierFillerDao notifierFillerDao;
    private NotifierDao notifierDao;
//    private TraitNotifierFillerDao traitNotifierFillerDao;
    private LiveData<List<TraitResult>> liveTraitEntities;
    private LiveData<List<TraitNotifierFillerResult>> liveNotifierFillerEntities;

    public TraitDefinitionRepository(Context context) {
        localDatabase = LocalDatabase.getInstance(context);
        traitDefinitionDao = localDatabase.traitDefinitionDao();
//        traitFillerDao = localDatabase.traitFillerDao();
//        notifierFillerDao = localDatabase.notifierFillerDao();
        notifierDao = localDatabase.notifierDao();
//        traitNotifierFillerDao = localDatabase.traitNotifierFillerDao();
        liveTraitEntities = traitDefinitionDao.getAllLive();
    }

    public LiveData<List<TraitResult>> getAllLive() {
        return liveTraitEntities;
    }

    public List<Trait> getAll() { return traitDefinitionDao.getAll(); }

    public List<Trait> getLatestByAmount(int amount) {
        return traitDefinitionDao.getLatestByCount(amount);
    }

    public void insertAsync(Trait trait) {
        new CreateTraitDefinitionAsync(traitDefinitionDao).execute(trait);
    }

    public void updateAsync(Trait trait) {
        new UpdateTraitDefinitionAsync(traitDefinitionDao).execute(trait);
    }

    //TODO: Add foreign key cascade delete
    public void removeAsync(Trait trait) {
        new RemoveTraitDefinitionAsync(traitDefinitionDao).execute(trait);
    }

    private static class CreateTraitDefinitionAsync extends AsyncTask<Trait, Void, Void> {

        private TraitDefinitionDao traitDefinitionDao;
        public CreateTraitDefinitionAsync(TraitDefinitionDao traitDefinitionDao) { this.traitDefinitionDao = traitDefinitionDao; }
        @Override
        protected Void doInBackground(Trait... traits) {
            traitDefinitionDao.insert(traits[0]);
            return null;
        }
    }

    private static class UpdateTraitDefinitionAsync extends AsyncTask<Trait, Void, Void> {

        private TraitDefinitionDao traitDefinitionDao;
        public UpdateTraitDefinitionAsync(TraitDefinitionDao traitDefinitionDao) { this.traitDefinitionDao = traitDefinitionDao; }
        @Override
        protected Void doInBackground(Trait... traits) {
            traitDefinitionDao.update(traits[0]);
            return null;
        }
    }

    private static class RemoveTraitDefinitionAsync extends AsyncTask<Trait, Void, Void> {

        private TraitDefinitionDao traitDefinitionDao;
        public RemoveTraitDefinitionAsync(TraitDefinitionDao traitDefinitionDao) { this.traitDefinitionDao = traitDefinitionDao; }
        @Override
        protected Void doInBackground(Trait... traits) {
            traitDefinitionDao.delete(traits[0]);
            return null;
        }
    }

    public List<Notifier> getAllNotifiers() {
        return notifierDao.getAll();
    }

    public int insertNotifier(Notifier notifier) {
        return (int)notifierDao.insert(notifier);
    }


//    public int insertNotifierFiller(NotifierFiller notifierFiller) {
//        return (int) notifierFillerDao.insert(notifierFiller);
//    }

}
