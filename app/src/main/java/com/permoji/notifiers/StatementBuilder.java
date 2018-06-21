package com.permoji.notifiers;

import android.text.Html;
import android.widget.TextView;

import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;

import java.util.Random;

/**
 * Created by michael on 20/06/18.
 */

public class StatementBuilder {

    private static StatementBuilder instance;

    public static StatementBuilder get() {
        if(instance == null) {
            instance = new StatementBuilder();
        }
        return instance;
    }

    public void buildStatement(TraitResult traitResult, TextView textView, TraitNotifierFillerResult traitNotifierFillerResult) {
        String statementText = traitResult.traitStatement.get(0).getStatement();
        String fullStatement = "";
        String fillerType = traitResult.traitStatement.get(0).getPlaceholderType();

        int fillerCount = statementText.split("<"+fillerType+">").length - 1;

        if(fillerCount == 0) {
            fillerCount = 1;
        }

        //Personalise statement or filler
        int personaliseStatementWeight = traitResult.traitStatement.get(0).getPersonaliseWeight();
        Random random = new Random();
        //TODO: Implement weighted selector
        if (personaliseStatementWeight == 100) {
            statementText = statementText.replace("<name>", traitNotifierFillerResult.notifier.get(0).getName());
        }
        else if (personaliseStatementWeight == 0) {
            statementText = statementText.replace("<name>", "");
        }

        for(int fillerIdx = 0; fillerIdx < fillerCount; fillerIdx++) {
            if(fillerCount > traitNotifierFillerResult.notifierFillerResultList.size()) {
                fillerCount = traitNotifierFillerResult.notifierFillerResultList.size();
            }

            String fillerText = traitNotifierFillerResult.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getText();
            String fillerExtraText = traitNotifierFillerResult.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getPersonalisedText();


            if (statementText.endsWith(">.")) {
                statementText = statementText.replace(">.", ">");
            }
            else {
                fillerText = fillerText.replace(".", "");
            }

            if (personaliseStatementWeight == 100) {
                fillerText = fillerText.replace("<personal>", "");
            } else if (personaliseStatementWeight == 0) {
                fillerText = fillerText.replace("<personal>", fillerExtraText);
            }

            fillerText = "<font color='red'>" + fillerText.replace("<name>", traitNotifierFillerResult.notifier.get(0).getName()) + "</font>";

            if (statementText.contains("<")) {
                statementText = statementText.replaceFirst("<" + fillerType + ">", fillerText);
            } else {
                statementText = statementText + fillerText;
            }
        }

        fullStatement = statementText;

        textView.setText(Html.fromHtml(fullStatement), TextView.BufferType.SPANNABLE);
    }
}
