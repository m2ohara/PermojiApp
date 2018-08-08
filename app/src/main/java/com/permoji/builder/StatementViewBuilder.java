package com.permoji.builder;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;
import com.permoji.repository.TraitDefinitionRepository;

import java.util.Random;

/**
 * Created by michael on 20/06/18.
 */

public class StatementViewBuilder {

    //TODO: Encapsulate logic
    private static StatementViewBuilder instance;
    private static TraitDefinitionRepository traitDefinitionRepository;

    public static StatementViewBuilder get() {
        if(instance == null) {
            instance = new StatementViewBuilder();
        }
        return instance;
    }

    public static StatementViewBuilder get(Context context) {
        if(instance == null) {
            instance = new StatementViewBuilder();
        }
        return get();
    }

    public void buildStatement(TraitResult traitResult, TextView textView, TraitNotifierFillerResult traitNotifierFillerResult) {
        String statementText = traitResult.traitStatement.get(0).getStatement();
        String fullStatement = "";
        String fillerType = traitResult.traitStatement.get(0).getPlaceholderType();

        int fillerCount = statementText.split("<"+fillerType+">").length - 1;

        //Default to at least 1 if none detected due to human error
        if(fillerCount == 0) {
            fillerCount = 1;
        }
        if(fillerCount > traitNotifierFillerResult.notifierFillerResultList.size()) {
            fillerCount = traitNotifierFillerResult.notifierFillerResultList.size();
        }

        //Personalise statement or filler
        int personaliseStatementWeight = traitResult.traitStatement.get(0).getPersonaliseWeight();


        //TODO: Implement weighted selector
        if (personaliseStatementWeight == 100) {
            statementText = statementText.replace("<name>", traitNotifierFillerResult.notifier.get(0).getName());
        }
        else if (personaliseStatementWeight == 0) {
            statementText = statementText.replace("<name>", "");
        }

        for(int fillerIdx = 0; fillerIdx < fillerCount; fillerIdx++) {

            String fillerText = traitNotifierFillerResult.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getText();
            String fillerExtraText = traitNotifierFillerResult.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getPersonalisedText();
            boolean isPersonalised = traitNotifierFillerResult.notifierFillerResultList.get(fillerIdx).isPersonalised();

            if (statementText.endsWith(">.")) {
                statementText = statementText.replace(">.", ">");
            }
            else {
                fillerText = fillerText.replace(".", "");
            }

            //TODO: Replace uppercase letter with lowercase after <personal>
            if (personaliseStatementWeight == 100) {
                fillerText = fillerText.replace("<personal>", "");
            } else if (personaliseStatementWeight == 0 && isPersonalised) {
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


    public void setExistingView(TraitResult traitResult, TextView textView) {

        String statementText = traitResult.traitStatement.get(0).getStatement();
        String fullStatement = "";
        String fillerType = traitResult.traitStatement.get(0).getPlaceholderType();

        TraitNotifierFillerResult traitNotifierFiller = traitResult.getSelectedTraitNotifierFillerResult().get(0);

        int fillerCount = statementText.split("<"+fillerType+">").length - 1;

        if(fillerCount == 0) {
            fillerCount = 1;
        }
        if(fillerCount > traitNotifierFiller.notifierFillerResultList.size()) {
            fillerCount = traitNotifierFiller.notifierFillerResultList.size();
        }

        //Personalise statement or filler
        int personaliseStatementWeight = traitResult.traitStatement.get(0).getPersonaliseWeight();
        //TODO: Implement weighted selector
        if (personaliseStatementWeight == 100) {
            statementText = statementText.replace("<name>", traitNotifierFiller.notifier.get(0).getName());
        }
        else if (personaliseStatementWeight == 0) {
            statementText = statementText.replace("<name>", "");
        }

        Random random = new Random();
        int personalisedFiller = random.nextInt(fillerCount); //TODO: Retrieve from DB

        for(int fillerIdx = 0; fillerIdx < fillerCount; fillerIdx++) {

            String fillerText = traitNotifierFiller.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getText();
            String fillerExtraText = traitNotifierFiller.notifierFillerResultList.get(fillerIdx).traitFiller.get(0).getPersonalisedText();
            boolean isPersonalisedFiller = false;
            isPersonalisedFiller = fillerIdx == personalisedFiller ? true  :false; //TODO: Implement traitNotifierFiller.notifierFillerResultList.get(fillerIdx).traitFiller.get(0)

            if (statementText.endsWith(">.")) {
                statementText = statementText.replace(">.", ">");
            }
            else {
                fillerText = fillerText.replace(".", "");
            }

            if (personaliseStatementWeight == 100) {
                fillerText = fillerText.replace("<personal>", "");
            } else if (personaliseStatementWeight == 0) {
                if(isPersonalisedFiller)
                    fillerText = fillerText.replace("<personal>", fillerExtraText);
            }

            fillerText = fillerText.replace("<name>", traitNotifierFiller.notifier.get(0).getName());

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
