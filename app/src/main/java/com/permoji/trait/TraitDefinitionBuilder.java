package com.permoji.trait;

import android.content.Context;

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

        TraitDefinition traitDefinition = new TraitDefinition();
        traitDefinition.setDatecreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        List<TraitStatement> statementList = traitDefinitionRepository.getTraitStatementsByCodepoint(codepoint);

        TraitStatement traitStatement = statementList.get(new Random().nextInt(statementList.size()));
        traitDefinition.setStatement(traitStatement);

        traitDefinitionRepository.insert(traitDefinition);
    }

    public void addNotifierToRecentTrait(int codepoint, Notifier notifier) {

        //Pick random trait from latest
        List<TraitDefinition> traitDefinitions = traitDefinitionRepository.getLatestTraitDefinitionsByAmount(latestAmount);
        TraitDefinition traitDefinition = traitDefinitions.get(new Random().nextInt(traitDefinitions.size()));

        List<TraitFiller> traitFillers = traitDefinitionRepository.getTraitFillersByCodepoint(codepoint);
        TraitFiller traitFiller = traitFillers.get(new Random().nextInt(traitFillers.size()));

        TraitNotifierFiller traitNotifierFiller = new TraitNotifierFiller();
        traitNotifierFiller.setNotifier(checkNotifierExists(notifier));
        traitNotifierFiller.setTraitFiller(traitFiller);

        //TODO: Check for existing notifier filler
        traitDefinitionRepository.insertNotifierFiller(traitNotifierFiller);

        List<TraitNotifierFiller> currentTraitFillers = traitDefinition.getFillers();
        currentTraitFillers.add(traitNotifierFiller);
        traitDefinition.setFillers(currentTraitFillers);

        traitDefinitionRepository.update(traitDefinition);
    }

    private Notifier checkNotifierExists(Notifier notifier) {
        List<Notifier> notifierList = traitDefinitionRepository.getAllNotifiers();

        for(Notifier existingNotifier : notifierList) {
            if(existingNotifier.getImagePath().equals(notifier.getImagePath()) && existingNotifier.getName().equals(notifier.getName())) {
                //TODO: Resolve updated images, duplicate notifier names
                return existingNotifier;
            }
        }

        traitDefinitionRepository.insertNotifier(notifier);
        return  notifier;
    }
}
