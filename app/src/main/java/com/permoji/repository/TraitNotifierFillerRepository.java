package com.permoji.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.TraitNotifierFillerDao;
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
        return traitNotifierFillerDao.getLiveDataById(id);
    }

    public int insert(TraitNotifierFiller traitNotifierFiller) {
        return (int)traitNotifierFillerDao.insert(traitNotifierFiller);
    }

    public void deleteAsync(TraitNotifierFiller traitNotifierFiller) {
        new DeleteAsync(traitNotifierFillerDao).execute(traitNotifierFiller);
    }

    public void remove(TraitNotifierFiller traitNotifierFiller) {
        traitNotifierFillerDao.delete(traitNotifierFiller);
    }

    private static class DeleteAsync extends AsyncTask<TraitNotifierFiller, Void, Void> {

        private TraitNotifierFillerDao traitNotifierFillerDao;
        public DeleteAsync(TraitNotifierFillerDao traitNotifierFillerDao) { this.traitNotifierFillerDao = traitNotifierFillerDao; }

        @Override
        protected Void doInBackground(TraitNotifierFiller... traitNotifierFillers) {
            traitNotifierFillerDao.delete(traitNotifierFillers[0]);
            return null;
        }
    }
}
