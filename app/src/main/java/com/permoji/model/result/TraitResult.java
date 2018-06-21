package com.permoji.model.result;

import android.arch.persistence.room.Relation;

import com.permoji.model.TraitNotifierFiller;
import com.permoji.model.TraitStatement;

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
}
