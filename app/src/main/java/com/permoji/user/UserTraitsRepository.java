package com.permoji.user;

import android.app.Application;
import android.arch.lifecycle.LiveData;
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
        traitsByUserId = traitDao.getAll();
    }

    public LiveData<List<Trait>> getTraitsByUserId(int id) {
        return traitsByUserId;
    }

    public void insert(Trait trait) {
        new insertTraitAsync(traitDao).execute(trait);
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
}
