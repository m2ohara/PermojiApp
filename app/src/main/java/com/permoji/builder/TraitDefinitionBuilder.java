package com.permoji.builder;

import android.content.Context;
import android.util.Log;

import com.permoji.model.entity.Notifier;
import com.permoji.model.entity.NotifierFiller;
import com.permoji.model.entity.TraitDefinition;
import com.permoji.repository.TraitDefinitionRepository;
import com.permoji.model.entity.TraitFiller;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.entity.TraitStatement;
import com.permoji.repository.TraitNotifierFillerRepository;

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
    private int latestAmount = 5;
    private int maxFillerAmount = 3;

    public TraitDefinitionBuilder(Context context) {
        traitDefinitionRepository = new TraitDefinitionRepository(context);
        traitNotifierFillerRepository = new TraitNotifierFillerRepository(context);
    }

    public void createTrait(int codepoint) {

        Log.d(this.getClass().getSimpleName(), "Processing keyboard broadcast for emoji "+Long.toHexString(codepoint));

        TraitDefinition traitDefinition = new TraitDefinition();
        traitDefinition.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        //TODO: Prioristise statements query by least amount of fillers
        List<TraitStatement> statementList = traitDefinitionRepository.getTraitStatementsByCodepoint(codepoint);
        if(statementList.size() > 0) {
            TraitStatement traitStatement = statementList.get(new Random().nextInt(statementList.size()));

            traitDefinition.setStatementId(traitStatement.getId());
            traitDefinitionRepository.insertAsync(traitDefinition);
        }
    }

    public void addNotifierToRecentTrait(int codepoint, Notifier notifier) {

        Log.d(this.getClass().getSimpleName(), "Processing notifier broadcast");

        TraitNotifierFiller traitNotifierFiller = new TraitNotifierFiller();

        //Pick random trait from latest
        List<TraitDefinition> traitDefinitions = traitDefinitionRepository.getLatestTraitDefinitionsByAmount(latestAmount);
        if(traitDefinitions.size() > 0) {
            TraitDefinition traitDefinition = traitDefinitions.get(new Random().nextInt(traitDefinitions.size()));

            List<TraitFiller> traitFillersUnfiltered = traitDefinitionRepository.getTraitFillersByCodepoint(codepoint);
            String fillerType = traitDefinitionRepository.getTraitStatementById(traitDefinition.getStatementId()).getPlaceholderType();
            List<TraitFiller> traitFillers = filterTraitFillersByPlaceHolderType(fillerType, traitFillersUnfiltered);

            traitNotifierFiller.setNotifierId(checkNotifierExists(notifier));
            traitNotifierFiller.setTraitDefinitionId(traitDefinition.getId());

            //TODO: Check for existing notifier filler
            int traitNotifierFillerId = -1;
            traitNotifierFillerId = traitNotifierFillerRepository.insert(traitNotifierFiller);

            for(int idx = 0; idx < maxFillerAmount; idx++) {
                TraitFiller traitFiller = traitFillers.remove(new Random().nextInt(traitFillers.size()));
                NotifierFiller notifierFiller = new NotifierFiller();
                notifierFiller.setTraitNotifierFillerId(traitNotifierFillerId);
                notifierFiller.setFillerId(traitFiller.getId());

                traitDefinitionRepository.insertNotifierFiller(notifierFiller);


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
        List<Notifier> notifierList = traitDefinitionRepository.getAllNotifiers();

        if(notifierList.size() > 0) {
            for (Notifier existingNotifier : notifierList) {
                if (existingNotifier.getImagePath().equals(notifier.getImagePath()) && existingNotifier.getName().equals(notifier.getName())) {
                    //TODO: Resolve updated images, duplicate notifier names
                    return existingNotifier.getId();
                }
            }
        }

        return traitDefinitionRepository.insertNotifier(notifier);
    }
}
