package com.permoji.database.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.permoji.builder.TraitDefinitionBuilder;
import com.permoji.database.LocalDatabase;
import com.permoji.database.dao.NotifierDao;
import com.permoji.database.dao.NotifierFillerDao;
import com.permoji.database.dao.TraitDefinitionDao;
import com.permoji.database.dao.TraitFillerDao;
import com.permoji.database.dao.TraitNotifierFillerDao;
import com.permoji.database.dao.TraitStatementDao;
import com.permoji.model.entity.Notifier;
import com.permoji.model.entity.NotifierFiller;
import com.permoji.model.entity.Trait;
import com.permoji.model.entity.TraitFiller;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.entity.TraitStatement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by michael on 25/09/18.
 */

public class InsertDefaultTraitAsync extends AsyncTask<Void, Void, Void> {

    private String packageName;
    private DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int traitAmountToGet = 5;
    private int statementsToFillAmount = 3;
    private TraitDefinitionDao traitDefinitionDao;
    private NotifierDao notifierDao;
    private TraitStatementDao traitStatementDao;
    private TraitFillerDao traitFillerDao;
    private TraitNotifierFillerDao traitNotifierFillerDao;
    private NotifierFillerDao notifierFillerDao;

    public InsertDefaultTraitAsync(LocalDatabase localDatabase, String packageName) {
        this.packageName = packageName;
        this.traitDefinitionDao = localDatabase.traitDefinitionDao();
        this.notifierDao = localDatabase.notifierDao();
        this.traitStatementDao = localDatabase.traitStatementDao();
        this.traitFillerDao = localDatabase.traitFillerDao();
        this.traitNotifierFillerDao = localDatabase.traitNotifierFillerDao();
        this.notifierFillerDao = localDatabase.notifierFillerDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        createTrait(2056223);

        Notifier notifier = new Notifier();
        notifier.setName("Permoji");
        notifier.setImagePath("android.resource://" + packageName + "/raw/permoji.png");

        addNotifierToRecentTrait(2056223, notifier);

        return null;
    }

    public void createTrait(int codepoint) {

        Log.d(this.getClass().getSimpleName(), "Processing keyboard broadcast for emoji "+Long.toHexString(codepoint));

        Trait trait = new Trait();
        trait.setDateCreated(simpleDateFormat.format(new Date()));

        //TODO: Prioristise statements query by least amount of fillers
        List<TraitStatement> statementList =  traitStatementDao.getDefault();
        List<Trait> currentTraits = traitDefinitionDao.getAllTraits();

        statementList = filterExistingStatements(statementList, currentTraits);

        createTrait(statementList, trait);

    }


    private List<TraitStatement> filterExistingStatements(List<TraitStatement> statementList, List<Trait> currentTraits) {
        //Filter any existing statements & remove
        List<TraitStatement> statementsToRemove = new ArrayList<>();
        for(TraitStatement traitStatement : statementList) {
            for (Trait currentTrait : currentTraits) {
                if (currentTrait.getStatementId() == traitStatement.getId()) {
                    statementsToRemove.add(traitStatement);
                }
            }
        }
        if(statementsToRemove.size() != statementList.size()) {
            statementList.removeAll(statementsToRemove);
        }

        return statementList;
    }

    private void createTrait(List<TraitStatement> statementList, Trait trait) {
        if(statementList.size() > 0) {
            TraitStatement traitStatement = statementList.get(new Random().nextInt(statementList.size()));

            trait.setStatementId(traitStatement.getId());
            traitDefinitionDao.insert(trait);
        }
    }

    public void addNotifierToRecentTrait(int codepoint, Notifier notifier) {

        List<Trait> traits = traitDefinitionDao.getLatestTraitsByCount(traitAmountToGet);
        if(traits.size() > 0) {
            //Pick random trait from latest
            Trait trait = traits.get(new Random().nextInt(traits.size()));

            List<TraitFiller> traitFillers = traitFillerDao.getAll();
            TraitStatement statement = traitStatementDao.getById(trait.getStatementId());

            traitFillers = filterTraitFillersByPlaceHolderType(statement.getPlaceholderType(), traitFillers);

            int notifierId = checkNotifierExists(notifier);
            if(notifierId == -1) {
                return;
            }

            TraitNotifierFiller traitNotifierFiller = new TraitNotifierFiller();
            traitNotifierFiller.setNotifierId(notifierId);
            traitNotifierFiller.setTraitDefinitionId(trait.getId());

            int traitNotifierFillerId = -1;
            traitNotifierFillerId = (int)traitNotifierFillerDao.insert(traitNotifierFiller);

            int fillerCount = statement.getStatement().split("<"+statement.getPlaceholderType()+">").length - 1;

            //Default to at least 1 if none detected due to human error
            if(fillerCount == 0) {
                fillerCount = 1;
            }

            Random random = new Random();
            int personalisedFiller = random.nextInt(fillerCount);

            for(int idx = 0; idx < statementsToFillAmount; idx++) {
                TraitFiller traitFiller = traitFillers.remove(new Random().nextInt(traitFillers.size()));
                NotifierFiller notifierFiller = new NotifierFiller();
                notifierFiller.setTraitNotifierFillerId(traitNotifierFillerId);
                notifierFiller.setFillerId(traitFiller.getId());
                if(idx == personalisedFiller) {
                    notifierFiller.setPersonalised(true);
                }
                notifierFillerDao.insert(notifierFiller);

            }
        }
    }

    private List<TraitFiller> filterTraitFillersByPlaceHolderType(String placeHolder, List<TraitFiller> traitFillerList) {
        List<TraitFiller> filteredList = new ArrayList<TraitFiller>();
        for (TraitFiller traitFiller : traitFillerList) {
            if(traitFiller.getPlaceholderType().equals(placeHolder)) {
                filteredList.add(traitFiller);
            }
        }

        if(filteredList.size() == 0) {
            return traitFillerList;
        }
        return filteredList;
    }

    private int checkNotifierExists(Notifier notifier) {
        List<Notifier> notifierList = notifierDao.getAll();

        if(notifierList.size() > 0) {
            for (Notifier existingNotifier : notifierList) {
                if (existingNotifier.getName().equals(notifier.getName())) {
                    //TODO: Resolve updated images, duplicate notifier names
                    return existingNotifier.getId();
                }
            }
        }

        if(notifier.getImagePath() != null && !notifier.getImagePath().isEmpty()) {
            return (int)notifierDao.insert(notifier);
        }

        return -1;
    }
}
