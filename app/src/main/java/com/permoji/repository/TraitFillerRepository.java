package com.permoji.repository;

import android.content.Context;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.TraitFillerDao;
import com.permoji.model.entity.TraitFiller;

import java.util.List;

/**
 * Created by michael on 22/06/18.
 */

public class TraitFillerRepository {

    private TraitFillerDao traitFillerDao;

    public TraitFillerRepository(Context context) {
        LocalDatabase db = LocalDatabase.getInstance(context);
        traitFillerDao = db.traitFillerDao();
    }

    public List<TraitFiller> getById(int id) {
        return traitFillerDao.getById(id);
    }

    public List<TraitFiller> getByCodepoint(int codepoint) {
        String unicodeValue = "U+"+Long.toHexString(codepoint).toUpperCase();
        return traitFillerDao.getByCodepoint(unicodeValue); //TODO:Temporary
    }

    public List<TraitFiller> getByCodepoint(String codepoint) {
        return traitFillerDao.getByCodepoint(codepoint); //TODO:Temporary
    }

    public List<TraitFiller> getAll() {
        return traitFillerDao.getAll();
    }
}
