package com.permoji.builder;

import android.content.Context;
import android.util.Log;

import com.permoji.model.entity.Notifier;
import com.permoji.model.entity.NotifierFiller;
import com.permoji.model.entity.Trait;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;
import com.permoji.repository.NotifierFillerRepository;
import com.permoji.repository.TraitDefinitionRepository;
import com.permoji.model.entity.TraitFiller;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.entity.TraitStatement;
import com.permoji.repository.TraitFillerRepository;
import com.permoji.repository.TraitNotifierFillerRepository;
import com.permoji.repository.TraitStatementRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by michael on 11/06/18.
 */

public class TraitDefinitionBuilder {
    private TraitDefinitionRepository traitDefinitionRepository;
    private TraitNotifierFillerRepository traitNotifierFillerRepository;
    private TraitFillerRepository traitFillerRepository;
    private TraitStatementRepository traitStatementRepository;
    private NotifierFillerRepository notifierFillerRepository;
    private int traitAmountToGet = 5;
    private int traitLimit = 10;
    private DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TraitDefinitionBuilder(Context context) {
        traitDefinitionRepository = new TraitDefinitionRepository(context);
        traitNotifierFillerRepository = new TraitNotifierFillerRepository(context);
        traitFillerRepository = new TraitFillerRepository(context);
        traitStatementRepository = new TraitStatementRepository(context);
        notifierFillerRepository = new NotifierFillerRepository(context);
    }

    public void createTrait(int codepoint) {

        Log.d(this.getClass().getSimpleName(), "Processing keyboard broadcast for emoji "+Long.toHexString(codepoint));

        Trait trait = new Trait();
        trait.setDateCreated(simpleDateFormat.format(new Date()));

        //TODO: Prioristise statements query by least amount of fillers
        List<TraitStatement> statementList = traitStatementRepository.getByCodepoint(codepoint);
        List<TraitResult> currentTraits = traitDefinitionRepository.getAll();

        currentTraits = removeOldestTraitIfLimit(currentTraits);

        statementList = filterExistingStatements(statementList, currentTraits);

        createTrait(statementList, trait);

    }

    private List<TraitResult> removeOldestTraitIfLimit(List<TraitResult> currentTraits) {

        if(currentTraits.size() == traitLimit) {
            
            Trait traitToRemove = new Trait();
            traitToRemove.setDateCreated(simpleDateFormat.format(new Date()));

            for(TraitResult currentTrait : currentTraits) {
                try {
                    if(simpleDateFormat.parse(currentTrait.getDateCreated())
                            .compareTo(simpleDateFormat.parse(traitToRemove.getDateCreated())) == -1) {
                        traitToRemove.setId(currentTrait.getId());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            currentTraits.remove(traitToRemove);
            traitDefinitionRepository.removeAsync(traitToRemove);
        }

        return currentTraits;


    }

    private List<TraitStatement> filterExistingStatements(List<TraitStatement> statementList, List<TraitResult> currentTraits) {
        //Filter any existing statements & remove
        List<TraitStatement> statementsToRemove = new ArrayList<>();
        for(TraitStatement traitStatement : statementList) {
            for (TraitResult currentTrait : currentTraits) {
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
            traitDefinitionRepository.insertAsync(trait);
        }
    }

    public void addNotifierToRecentTrait(Notifier notifier) {

        Log.d(this.getClass().getSimpleName(), "Processing notifier broadcast");

        List<TraitResult> traits = traitDefinitionRepository.getLatestByAmount(traitAmountToGet);
        if(traits.size() > 0) {
            //Pick random trait from latest
            TraitResult trait = traits.get(new Random().nextInt(traits.size()));

            if(notifierIsMostRecentInTrait(trait, notifier)) {
                return;
            }

            List<TraitFiller> traitFillers = traitFillerRepository.getAll();
            TraitStatement statement = traitStatementRepository.getById(trait.getStatementId());

            traitFillers = filterTraitFillersByPlaceHolderType(statement.getPlaceholderType(), traitFillers);

            int notifierId = checkNotifierExists(notifier);
            if(notifierId == -1) {
                return;
            }

            TraitNotifierFiller traitNotifierFiller = new TraitNotifierFiller();
            traitNotifierFiller.setNotifierId(notifierId);
            traitNotifierFiller.setTraitDefinitionId(trait.getId());

            int traitNotifierFillerId = -1;
            traitNotifierFillerId = traitNotifierFillerRepository.insert(traitNotifierFiller);

            int fillerCount = statement.getStatement().split("<"+statement.getPlaceholderType()+">").length - 1;
            addPersonalisedFillers(fillerCount, traitNotifierFillerId, traitFillers);

//            //Default to at least 1 if none detected due to human error
//            if(fillerCount == 0) {
//                fillerCount = 1;
//            }
//
//            Random random = new Random();
//            int personalisedFiller = random.nextInt(fillerCount);
//
//            for(int idx = 0; idx < statementsToFillAmount; idx++) {
//                TraitFiller traitFiller = traitFillers.remove(new Random().nextInt(traitFillers.size()));
//                NotifierFiller notifierFiller = new NotifierFiller();
//                notifierFiller.setTraitNotifierFillerId(traitNotifierFillerId);
//                notifierFiller.setFillerId(traitFiller.getId());
//                if(idx == personalisedFiller) {
//                    notifierFiller.setPersonalised(true);
//                }
//                notifierFillerRepository.insert(notifierFiller);
//
//            }
        }
    }

    private void addPersonalisedFillers(int fillerCount, int traitNotifierFillerId, List<TraitFiller> traitFillers) {
        //Default to at least 1 if none detected due to human error
        if(fillerCount == 0) {
            fillerCount = 1;
        }

        Random random = new Random();
        int personalisedFiller = random.nextInt(fillerCount);

        List<NotifierFiller> notifierFillers = new ArrayList<>();

        for(int idx = 0; idx < fillerCount; idx++) {
            NotifierFiller notifierFiller = new NotifierFiller();
            notifierFiller.setTraitNotifierFillerId(traitNotifierFillerId);

            TraitFiller traitFiller = traitFillers.remove(new Random().nextInt(traitFillers.size()));
            notifierFiller.setFillerId(traitFiller.getId());
            if(idx == personalisedFiller) {
                notifierFiller.setPersonalised(true);
            }
            notifierFillers.add(notifierFiller);
        }

        notifierFillerRepository.insertAll(notifierFillers);
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
        List<Notifier> notifierList = traitDefinitionRepository.getAllNotifiers();

        if(notifierList.size() > 0) {
            for (Notifier existingNotifier : notifierList) {
                if (existingNotifier.getName().equals(notifier.getName())) {
                    //TODO: Resolve updated images, duplicate notifier names
                    return existingNotifier.getId();
                }
            }
        }

        if(notifier.getImagePath() != null && !notifier.getImagePath().isEmpty()) {
            return traitDefinitionRepository.insertNotifier(notifier);
        }

        return -1;
    }

    private boolean notifierIsMostRecentInTrait(TraitResult traitResult, Notifier notifier) {
        if(traitResult.traitNotifierFillerResultList.size() > 0) {
            TraitNotifierFillerResult traitNotifierFillerResult = traitResult.traitNotifierFillerResultList.get(traitResult.traitNotifierFillerResultList.size() - 1);
            return (traitNotifierFillerResult.notifier.get(0).getName().equals(notifier.getName()));
        }
        return false;

    }
}
