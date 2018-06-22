package com.permoji.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.permoji.cache.LocalDatabase;
import com.permoji.cache.dao.TraitNotifierFillerDao;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.result.TraitNotifierFillerResult;

import java.util.List;

/**
 * Created by michael on 21/06/18.
 */

public class TraitNotifierFillerRepository {

    private TraitNotifierFillerDao traitNotifierFillerDao;

    public TraitNotifierFillerRepository(Context context) {
        this.traitNotifierFillerDao = LocalDatabase.getInstance(context).traitNotifierFillerDao();
    }

    public LiveData<List<TraitNotifierFillerResult>> getLiveByTraitDefinitionId(int id) {
        return traitNotifierFillerDao.getNotificationFillersByTraitDefinitionId(id);
    }

    public int insert(TraitNotifierFiller traitNotifierFiller) {
        return (int)traitNotifierFillerDao.insert(traitNotifierFiller);
    }

    public void removeAsync(TraitNotifierFiller traitNotifierFiller) {
        new RemoveAsync(traitNotifierFillerDao).execute(traitNotifierFiller);
    }

    public void remove(TraitNotifierFiller traitNotifierFiller) {
        traitNotifierFillerDao.delete(traitNotifierFiller);
    }

    private static class RemoveAsync extends AsyncTask<TraitNotifierFiller, Void, Void> {

        private TraitNotifierFillerDao traitNotifierFillerDao;
        public RemoveAsync(TraitNotifierFillerDao traitNotifierFillerDao) { this.traitNotifierFillerDao = traitNotifierFillerDao; }

        @Override
        protected Void doInBackground(TraitNotifierFiller... traitNotifierFillers) {
            traitNotifierFillerDao.delete(traitNotifierFillers[0]);
            return null;
        }
    }
}
