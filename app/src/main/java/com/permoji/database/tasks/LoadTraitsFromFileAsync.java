package com.permoji.database.tasks;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.TraitFillerDao;
import com.permoji.database.dao.TraitStatementDao;
import com.permoji.model.entity.TraitFiller;
import com.permoji.model.entity.TraitStatement;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by michael on 12/06/18.
 */

public class LoadTraitsFromFileAsync extends AsyncTask<Void, Void, Void> {

    private TraitStatementDao traitStatementDao;
    private TraitFillerDao traitFillerDao;
    private Resources resources;
    private String resourcePackageName;

    public LoadTraitsFromFileAsync(LocalDatabase db, Resources resources, String resourcePackageName) {
        this.traitStatementDao = db.traitStatementDao();
        this.traitFillerDao = db.traitFillerDao();
        this.resources = resources;
        this.resourcePackageName = resourcePackageName;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        if(traitStatementDao.getTraitStatementCount() > 0) {
            return null;
        }

        Log.i(this.getClass().getSimpleName(), "Loading files to DB");

        try {

            Gson gson = new Gson();

            List<TraitStatement> traitStatements = gson.fromJson(loadJsonFile("trait_statements"), new TypeToken<List<TraitStatement>>() {
            }.getType());
            traitStatementDao.InsertAll(traitStatements);

            List<TraitFiller> traitFillers = gson.fromJson(loadJsonFile("trait_fillers"), new TypeToken<List<TraitFiller>>() {
            }.getType());
            traitFillerDao.InsertAll(traitFillers);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), "Error reading file to database "+e.getMessage());
        }

        return null;
    }

    private String loadJsonFile(String resourceName) {

        String json = null;
        InputStream is = resources.openRawResource(resources.getIdentifier(resourceName, "raw", resourcePackageName));
        try {

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(), "Error loading file "+e.getMessage());
        }

        return json;
    }
}
