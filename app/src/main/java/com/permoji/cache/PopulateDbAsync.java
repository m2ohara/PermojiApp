package com.permoji.cache;

import android.os.AsyncTask;

import com.permoji.api.trait.Trait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        ArrayList<String> voters1 = new ArrayList<>(); voters1.addAll(Arrays.asList("contactimage4", "contactimage3", "contactimage6"));
        Trait trait1 = new Trait(); trait1.setCodepoint(0x1F601); trait1.setDescription("Cheerful"); trait1.setVoucherImageNames(voters1);
        traitDao.insert(trait1);

        ArrayList<String> voters2 = new ArrayList<>(); voters2.addAll(Arrays.asList("contactimage5", "contactimage2", "contactimage1", "contactimage3"));
        Trait trait2 = new Trait(); trait2.setCodepoint(0x1F602); trait2.setDescription("Joker"); trait2.setVoucherImageNames(voters2);
        traitDao.insert(trait2);

        ArrayList<String> voters3 = new ArrayList<>(); voters3.addAll( Arrays.asList("contactimage3", "contactimage1", "contactimage7", "contactimage8"));
        Trait trait3 = new Trait(); trait3.setCodepoint(0x1F604); trait3.setDescription("Princess"); trait3.setVoucherImageNames(voters3);
        traitDao.insert(trait3);

        ArrayList<String> voters4 = new ArrayList<>(); voters4.addAll( (Arrays.asList("contactimage9", "contactimage8", "contactimage7", "contactimage5")));
        Trait trait4 = new Trait(); trait4.setCodepoint(0x1F608); trait4.setDescription("Cheeky"); trait4.setVoucherImageNames(voters4);
        traitDao.insert(trait4);

        return null;
    }
}
