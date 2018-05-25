package com.permoji.cache;

import android.os.AsyncTask;

import com.permoji.api.trait.Trait;

/**
 * Created by michael on 25/05/18.
 */

public class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private TraitDao traitDao;

    PopulateDbAsync(LocalDatabase db) {
        this.traitDao = db.traitDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        traitDao.deleteAll();

        Trait trait1 = new Trait(); trait1.setCodepoint(0x1F601); trait1.setDescription("Cheerful");
        traitDao.insert(trait1);

        Trait trait2 = new Trait(); trait2.setCodepoint(0x1F602); trait2.setDescription("Joker");
        traitDao.insert(trait2);

        Trait trait3 = new Trait(); trait3.setCodepoint(0x1F604); trait3.setDescription("Yada");
        traitDao.insert(trait3);

        Trait trait4 = new Trait(); trait4.setCodepoint(0x1F608); trait4.setDescription("Something");
        traitDao.insert(trait4);

        return null;
    }
}
