package com.permoji.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by michael on 11/06/18.
 */

@Entity(tableName = "trait_definition")
public class Trait {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer statementId;
    private Integer selectedFillerId;
    private String dateCreated;
    private Integer personalisedIndex;

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

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

    public Integer getPersonalisedIndex() {
        return personalisedIndex;
    }

    public void setPersonalisedIndex(Integer personalisedIndex) {
        this.personalisedIndex = personalisedIndex;
    }
}
