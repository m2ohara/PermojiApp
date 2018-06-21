package com.permoji.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.permoji.cache.LocalDatabase;
import com.permoji.cache.dao.NotifierDao;
import com.permoji.cache.dao.NotifierFillerDao;
import com.permoji.cache.dao.TraitDefinitionDao;
import com.permoji.cache.dao.TraitFillerDao;
import com.permoji.cache.dao.TraitNotifierFillerDao;
import com.permoji.cache.dao.TraitStatementDao;
import com.permoji.model.Notifier;
import com.permoji.model.NotifierFiller;
import com.permoji.model.TraitDefinition;
import com.permoji.model.TraitFiller;
import com.permoji.model.TraitNotifierFiller;
import com.permoji.model.TraitStatement;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by michael on 11/06/18.
 */

public class TraitDefinitionRepository {

    //TODO: Separate into individual repos based on tables
    private LocalDatabase localDatabase;
    private TraitDefinitionDao traitDefinitionDao;
    private TraitStatementDao traitStatementDao;
    private TraitFillerDao traitFillerDao;
    private NotifierFillerDao notifierFillerDao;
    private NotifierDao notifierDao;
    private TraitNotifierFillerDao traitNotifierFillerDao;
    private LiveData<List<TraitResult>> liveTraitEntities;
    private LiveData<List<TraitNotifierFillerResult>> liveNotifierFillerEntities;

    public TraitDefinitionRepository(Context context) {
        localDatabase = LocalDatabase.getInstance(context);
        traitDefinitionDao = localDatabase.traitDefinitionDao();
        traitStatementDao = localDatabase.traitStatementDao();
        traitFillerDao = localDatabase.traitFillerDao();
        notifierFillerDao = localDatabase.notifierFillerDao();
        notifierDao = localDatabase.notifierDao();
        traitNotifierFillerDao = localDatabase.traitNotifierFillerDao();
        liveTraitEntities = traitDefinitionDao.getAllTraitViewModels();
    }

    public LiveData<List<TraitResult>> getLiveTraitEntities() {
        return liveTraitEntities;
    }

    public List<TraitDefinition> getLatestTraitDefinitionsByAmount(int amount) {
        return traitDefinitionDao.getLatestByCount(amount);
    }

    public void insertAsync(TraitDefinition traitDefinition) {
        new CreateTraitDefinitionAsync(traitDefinitionDao).execute(traitDefinition);
    }

    public void updateAsync(TraitDefinition traitDefinition) {
        new UpdateTraitDefinitionAsync(traitDefinitionDao).execute(traitDefinition);
    }

    private static class CreateTraitDefinitionAsync extends AsyncTask<TraitDefinition, Void, Void> {

        private TraitDefinitionDao traitDefinitionDao;
        public CreateTraitDefinitionAsync(TraitDefinitionDao traitDefinitionDao) { this.traitDefinitionDao = traitDefinitionDao; }
        @Override
        protected Void doInBackground(TraitDefinition... traitDefinitions) {
            traitDefinitionDao.insert(traitDefinitions[0]);
            return null;
        }
    }

    private static class UpdateTraitDefinitionAsync extends AsyncTask<TraitDefinition, Void, Void> {

        private TraitDefinitionDao traitDefinitionDao;
        public UpdateTraitDefinitionAsync(TraitDefinitionDao traitDefinitionDao) { this.traitDefinitionDao = traitDefinitionDao; }
        @Override
        protected Void doInBackground(TraitDefinition... traitDefinitions) {
            traitDefinitionDao.update(traitDefinitions[0]);
            return null;
        }
    }

    public TraitStatement getTraitStatementById(int id) {
        return traitStatementDao.getById(id);
    }

    public List<TraitStatement> getTraitStatementsByCodepoint(int codepoint) {
        String unicodeValue = "U+"+Long.toHexString(codepoint).toUpperCase();
        return traitStatementDao.getByCodepoint(unicodeValue);
    }

    public List<TraitFiller> getTraitFillersByCodepoint(int codepoint) {
        String unicodeValue = "U+"+Long.toHexString(codepoint).toUpperCase();
//        return traitFillerDao.getByCodepoint(unicodeValue); //TODO:Temporary
        return traitFillerDao.getByPopularityWeight();
    }

    public List<Notifier> getAllNotifiers() {
        return notifierDao.getAll();
    }

    public int insertNotifier(Notifier notifier) {
        return (int)notifierDao.insert(notifier);
    }

    public int insertNotifierAsync(Notifier notifier) {

        try {
           return new CreateNotifierAsync(notifierDao).execute(notifier).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static class CreateNotifierAsync extends AsyncTask<Notifier, Void, Integer> {

        //Will not return if in async invoked by async task
        private NotifierDao notifierDao;
        public CreateNotifierAsync(NotifierDao notifierDao) { this.notifierDao = notifierDao; }
        @Override
        protected Integer doInBackground(Notifier... notifiers) {
            int id = (int)notifierDao.insert(notifiers[0]);
            return id;
        }

    }

    public int insertTraitNotifierFiller(TraitNotifierFiller traitNotifierFiller) {
        return (int)traitNotifierFillerDao.insert(traitNotifierFiller);
    }

    private static class CreateTraitNotifierFillerAsync extends AsyncTask<TraitNotifierFiller, Void, Integer> {

        private TraitNotifierFillerDao traitNotifierFillerDao;
        public CreateTraitNotifierFillerAsync(TraitNotifierFillerDao traitNotifierFillerDao) { this.traitNotifierFillerDao = traitNotifierFillerDao;}

        @Override
        protected Integer doInBackground(TraitNotifierFiller... traitNotifierFillers) {
            return (int)traitNotifierFillerDao.insert(traitNotifierFillers[0]);
        }
    }

    public LiveData<List<TraitNotifierFillerResult>> getLiveNotifierFillersByTraitDefinitionId(int id) {
        return traitNotifierFillerDao.getNotificationFillersByTraitDefinitionId(id);
    }

    public int insertNotifierFiller(NotifierFiller notifierFiller) {
        return (int) notifierFillerDao.insert(notifierFiller);
    }

}
