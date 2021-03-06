package com.permoji.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.TraitAdjustPropertiesDao;
import com.permoji.model.entity.TraitAdjustProperties;

import java.util.List;

/**
 * Created by michael on 21/06/18.
 */

public class TraitAdjustPropertiesRepository {

    private TraitAdjustPropertiesDao traitAdjustPropertiesDao;

    public TraitAdjustPropertiesRepository(Context context) {
        traitAdjustPropertiesDao = LocalDatabase.getInstance(context).traitAdjustPropertiesDao();
    }

    public LiveData<List<TraitAdjustProperties>> getLiveTraitAdjustProperties() {
        return  traitAdjustPropertiesDao.getLiveTraitAdjustProperties();
    }

    public int getAdjustmentCount() {
        return  traitAdjustPropertiesDao.getTraitAdjustProperties().get(0).getCount();
    }

    public void increaseCountAsync(int increment) {
        new IncreaseCountAsync(traitAdjustPropertiesDao).execute(increment);
    }

    private class IncreaseCountAsync extends AsyncTask<Integer, Void, Void> {
        private TraitAdjustPropertiesDao traitAdjustPropertiesDao;
        public IncreaseCountAsync(TraitAdjustPropertiesDao traitAdjustPropertiesDao) { this.traitAdjustPropertiesDao = traitAdjustPropertiesDao;}

        @Override
        protected Void doInBackground(Integer... increment) {

            TraitAdjustProperties traitAdjustProperties = traitAdjustPropertiesDao.getTraitAdjustProperties().get(0);
            traitAdjustProperties.setCount(traitAdjustProperties.getCount() + increment[0]);

            traitAdjustPropertiesDao.update(traitAdjustProperties);
            return null;
        }
    }

    public void decreaseCount(int decrement) {
        TraitAdjustProperties traitAdjustProperties = traitAdjustPropertiesDao.getTraitAdjustProperties().get(0);
        traitAdjustProperties.setCount((traitAdjustProperties.getCount() - decrement) < 0 ? 0 : traitAdjustProperties.getCount() - decrement);

        traitAdjustPropertiesDao.update(traitAdjustProperties);
    }
}
