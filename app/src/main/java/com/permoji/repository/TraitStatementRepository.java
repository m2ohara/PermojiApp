package com.permoji.repository;

import android.content.Context;

import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.TraitDefinitionDao;
import com.permoji.database.dao.TraitStatementDao;
import com.permoji.model.entity.TraitStatement;

import java.util.List;

/**
 * Created by michael on 03/08/18.
 */

public class TraitStatementRepository {

    private TraitStatementDao traitStatementDao;

    public TraitStatementRepository(Context context) {
        traitStatementDao = LocalDatabase.getInstance(context).traitStatementDao();
    }

    public TraitStatement getById(int id) {
        return traitStatementDao.getById(id);
    }

    public List<TraitStatement> getByCodepoint(int codepoint) {
        String unicodeValue = "U+"+Long.toHexString(codepoint).toUpperCase();
        return traitStatementDao.getByCodepoint(unicodeValue);
    }
}
