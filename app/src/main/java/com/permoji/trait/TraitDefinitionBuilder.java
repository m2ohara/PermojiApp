package com.permoji.trait;

import android.content.Context;
import android.util.Log;

import com.permoji.trait.data.Notifier;
import com.permoji.trait.data.TraitDefinition;
import com.permoji.trait.data.TraitDefinitionRepository;
import com.permoji.trait.data.TraitFiller;
import com.permoji.trait.data.TraitNotifierFiller;
import com.permoji.trait.data.TraitStatement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by michael on 11/06/18.
 */

public class TraitDefinitionBuilder {
    private TraitDefinitionRepository traitDefinitionRepository;
    private int latestAmount = 5;

    public TraitDefinitionBuilder(Context context) {
        traitDefinitionRepository = new TraitDefinitionRepository(context);
    }

    public void createTrait(int codepoint) {

        Log.d(this.getClass().getSimpleName(), "Processing keyboard broadcast for emoji "+codepoint);

        TraitDefinition traitDefinition = new TraitDefinition();
        traitDefinition.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

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

            List<TraitFiller> traitFillers = traitDefinitionRepository.getTraitFillersByCodepoint(codepoint);
            TraitFiller traitFiller = traitFillers.get(new Random().nextInt(traitFillers.size()));

            traitNotifierFiller.setNotifierId(checkNotifierExists(notifier));
            traitNotifierFiller.setFillerId(traitFiller.getId());
            traitNotifierFiller.setTraitDefinitionId(traitDefinition.getId());

            //TODO: Check for existing notifier filler
            traitDefinitionRepository.insertTraitNotifierFiller(traitNotifierFiller);
        }
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
