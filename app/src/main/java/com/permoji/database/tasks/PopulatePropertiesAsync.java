package com.permoji.database.tasks;

import android.os.AsyncTask;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.TraitAdjustPropertiesDao;
import com.permoji.model.entity.TraitAdjustProperties;

/**
 * Created by michael on 22/06/18.
 */

public class PopulatePropertiesAsync extends AsyncTask<Void, Void, Void> {

    TraitAdjustPropertiesDao traitAdjustPropertiesDao;
    public PopulatePropertiesAsync(LocalDatabase db) {
        traitAdjustPropertiesDao = db.traitAdjustPropertiesDao();
    }
    @Override
    protected Void doInBackground(Void... voids) {

        if(traitAdjustPropertiesDao.getTraitAdjustProperties().size() == 0) {
            TraitAdjustProperties traitAdjustProperties = new TraitAdjustProperties();
            traitAdjustProperties.setCount(0);
            traitAdjustPropertiesDao.insert(traitAdjustProperties);
        }

        return null;
    }
}
