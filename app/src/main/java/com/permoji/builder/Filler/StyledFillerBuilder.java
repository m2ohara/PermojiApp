package com.permoji.builder.Filler;

import com.permoji.builder.Filler.IFillerBuilder;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;

/**
 * Created by michael on 05/10/18.
 */

public class StyledFillerBuilder implements IFillerBuilder
{
    @Override
    public String addFillerToStatement(TraitResult traitResult, TraitNotifierFillerResult traitNotifierFiller) {
        String statementText = traitResult.traitStatement.get(0).getStatement();
        String fillerType = traitResult.traitStatement.get(0).getPlaceholderType();

        int fillerCount = statementText.split("<"+fillerType+">").length - 1;

        if(fillerCount == 0) {
            fillerCount = 1;
        }
        if(fillerCount > traitNotifierFiller.notifierFillerResultList.size()) {
            fillerCount = traitNotifierFiller.notifierFillerResultList.size();
        }

        int personaliseStatementWeight = traitResult.traitStatement.get(0).getPersonaliseWeight();

        for(int fillerIdx = 0; fillerIdx < fillerCount; fillerIdx++) {

            String fillerText = traitNotifierFiller.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getText();
            String fillerExtraText = traitNotifierFiller.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getPersonalisedText();
            boolean isPersonalisedFiller = traitNotifierFiller.notifierFillerResultList.get(fillerIdx).isPersonalised();

            if (statementText.endsWith(">.")) {
                statementText = statementText.replace(">.", ">");
            }
            else {
                fillerText = fillerText.replace(".", "");
            }

            if (personaliseStatementWeight == 100) {
                fillerText = fillerText.replace("<personal>", "");
            } else if (personaliseStatementWeight == 0 && isPersonalisedFiller) {
                fillerText = fillerText.replace("<personal>", fillerExtraText);
            }

            fillerText = "<font color='red'>" + fillerText.replace("<name>", traitNotifierFiller.notifier.get(0).getName()) + "</font>";

            if (statementText.contains("<")) {
                statementText = statementText.replaceFirst("<" + fillerType + ">", fillerText);
            } else {
                statementText = statementText + fillerText;
            }
        }

        return statementText;
    }
}
