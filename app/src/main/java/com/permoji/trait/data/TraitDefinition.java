package com.permoji.trait.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by michael on 11/06/18.
 */

@Entity(tableName = "trait_definition", foreignKeys={
        @ForeignKey(
                entity=TraitStatement.class,
                parentColumns="id",
                childColumns="statementId"),
        @ForeignKey(
                entity=TraitFiller.class,
                parentColumns="id",
                childColumns="selectedFillerId")},
        indices=@Index(value="statementId"))
public class TraitDefinition {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer statementId;
    private Integer selectedFillerId;
    private String dateCreated;

//    @Relation(parentColumn = "id", entityColumn = "traitDefinitionId") //TODO: Move to container class
//    private List<TraitNotifierFiller> fillers;
//    private TraitStatement statement;
//    private TraitFiller selectedFiller;

//    public void setFillers(List<TraitNotifierFiller> fillers) {
//        this.fillers = fillers;
//    }

//    public List<TraitNotifierFiller> getFillers() {
//        return fillers;
//    }

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

}
