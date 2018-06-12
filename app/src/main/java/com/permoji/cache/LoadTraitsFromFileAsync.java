package com.permoji.cache;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permoji.trait.TraitFiller;
import com.permoji.trait.TraitStatement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 12/06/18.
 */

public class LoadTraitsFromFileAsync extends AsyncTask<Void, Void, Void> {

    private LocalDatabase db;
    private TraitStatementDao traitStatementDao;
    private TraitFillerDao traitFillerDao;
    private Context context;

    public LoadTraitsFromFileAsync(LocalDatabase db) {
        this.db = db;
        this.traitStatementDao = db.traitStatementDao();
        this.traitFillerDao = db.traitFillerDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Gson gson = new Gson();

        List<TraitStatement> traitStatements = gson.fromJson(loadJsonFile(R.raw.trait_statements), new TypeToken<List<TraitStatement>>(){}.getType());
        traitStatementDao.InsertAll(traitStatements);

        List<TraitFiller> traitFillers = gson.fromJson(loadJsonFile(R.raw.trait_fillers), new TypeToken<List<TraitFiller>>(){}.getType());
        traitFillerDao.InsertAll(traitFillers);

        return null;
    }

    private String loadJsonFile(int resourceId) {

        String json = null;
        InputStream is = context.getResources().openRawResource(resourceId);
        try {

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-16");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
