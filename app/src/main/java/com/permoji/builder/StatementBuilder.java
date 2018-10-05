package com.permoji.builder;

import android.text.Html;
import android.widget.TextView;

import com.permoji.builder.Filler.FillerBuilder;
import com.permoji.builder.Filler.IFillerBuilder;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;

/**
 * Created by michael on 05/10/18.
 */

public class StatementBuilder {

    private IFillerBuilder fillerBuilder;

    public StatementBuilder() {
        this.fillerBuilder = new FillerBuilder();
    }

    public StatementBuilder(IFillerBuilder fillerBuilder) {
        this.fillerBuilder = fillerBuilder;
    }

    public void setStatement(TextView textView, TraitResult traitResult, TraitNotifierFillerResult traitNotifierFillerResult) {
        String fullStatement = "";

        String statementText = personaliseStatement(traitResult, traitNotifierFillerResult);

        traitResult.traitStatement.get(0).setStatement(statementText);

        fullStatement = fillerBuilder.addFillerToStatement(traitResult, traitNotifierFillerResult);

        textView.setText(Html.fromHtml(fullStatement), TextView.BufferType.SPANNABLE);
    }

    private String personaliseStatement(TraitResult traitResult, TraitNotifierFillerResult traitNotifierFillerResult) {

        String statementText = traitResult.traitStatement.get(0).getStatement();

        int personaliseStatementWeight = traitResult.traitStatement.get(0).getPersonaliseWeight();

        if (personaliseStatementWeight == 100) {
            statementText = statementText.replace("<name>", traitNotifierFillerResult.notifier.get(0).getName());
        }
        else if (personaliseStatementWeight == 0) {
            statementText = statementText.replace("<name>", "");
        }

        return statementText;
    }

}
