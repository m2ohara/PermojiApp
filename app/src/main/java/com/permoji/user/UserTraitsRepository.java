package com.permoji.user;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.permoji.api.trait.Trait;
import com.permoji.cache.LocalDatabase;
import com.permoji.cache.TraitDao;

import java.util.List;

/**
 * Created by michael on 24/05/18.
 */

public class UserTraitsRepository {

    private TraitDao traitDao;
    private LiveData<List<Trait>> traitsByUserId;

    public UserTraitsRepository(Application application) {

        LocalDatabase db = LocalDatabase.getInstance(application);
        traitDao = db.traitDao();
        traitsByUserId = traitDao.getAllLive();
    }

    public UserTraitsRepository(Context context) {

        LocalDatabase db = LocalDatabase.getInstance(context);
        traitDao = db.traitDao();
        traitsByUserId = traitDao.getAllLive();
    }

    public LiveData<List<Trait>> getLiveTraitsByUserId(int id) {
        return traitsByUserId;
    }

    public List<Trait> getAllTraits() { return traitDao.getAll(); }

    public void insert(Trait trait) {
        new insertTraitAsync(traitDao).execute(trait);
    }

    public void update(Trait trait) {
        new updateTraitAsync(traitDao).execute(trait);
    }

    private static class insertTraitAsync extends AsyncTask<Trait, Void, Void> {

        private TraitDao asyncTraitDao;

        public insertTraitAsync(TraitDao asyncTraitDao) {
            this.asyncTraitDao = asyncTraitDao;
        }

        @Override
        protected Void doInBackground(Trait... traits) {
            asyncTraitDao.insert(traits[0]);
            return null;
        }
    }

    private static class updateTraitAsync extends AsyncTask<Trait, Void, Void> {

        private TraitDao asyncTraitDao;

        public updateTraitAsync(TraitDao asyncTraitDao) {
            this.asyncTraitDao = asyncTraitDao;
        }

        @Override
        protected Void doInBackground(Trait... traits) {
            asyncTraitDao.update(traits[0]);
            return null;
        }
    }
}
