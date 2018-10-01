package com.permoji.model.result;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.permoji.api.trait.Trait;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.entity.TraitStatement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michael on 15/06/18.
 */

public class TraitResult implements Serializable {

    private Integer id;
    private Integer statementId;
    private Integer selectedFillerId;
    private String dateCreated;
    @Relation(parentColumn = "statementId", entityColumn = "id", entity = TraitStatement.class)
    public List<TraitStatement> traitStatement;
    @Relation(parentColumn = "id", entityColumn = "traitDefinitionId", entity = TraitNotifierFiller.class)
    public List<TraitNotifierFillerResult> traitNotifierFillerResultList;
    @Relation(parentColumn = "selectedFillerId", entityColumn = "traitDefinitionId", entity = TraitNotifierFiller.class)
    private List<TraitNotifierFillerResult> selectedTraitNotifierFillerResult;
    @Ignore
    public boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatementId() {
        return statementId;
    }

    public void setStatementId(Integer statementId) {
        this.statementId = statementId;
    }

    public Integer getSelectedFillerId() {
        return selectedFillerId;
    }

    public void setSelectedFillerId(Integer selectedFillerId) {
        this.selectedFillerId = selectedFillerId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<TraitNotifierFillerResult> getSelectedTraitNotifierFillerResult() {
        return selectedTraitNotifierFillerResult;
    }

    public void setSelectedTraitNotifierFillerResult(List<TraitNotifierFillerResult> selectedTraitNotifierFillerResult) {
        this.selectedTraitNotifierFillerResult = selectedTraitNotifierFillerResult;
    }
}
