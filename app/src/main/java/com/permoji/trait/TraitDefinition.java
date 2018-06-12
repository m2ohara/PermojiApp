package com.permoji.trait;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/06/18.
 */

@Entity(tableName = "traitdefinition")
public class TraitDefinition {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer statementId;
    private Integer selectedFillerId;
    private ArrayList<Integer> fillerIds;
    private String datecreated;

    private TraitStatement statement;
    private List<TraitNotifierFiller> fillers;
    private TraitFiller selectedFiller;

    public TraitStatement getStatement() {
        return statement;
    }

    public void setStatement(TraitStatement statement) {
        this.statement = statement;
        this.statementId = statement.getId();
    }

    public List<TraitNotifierFiller> getFillers() {
        return fillers;
    }

    public void setFillers(List<TraitNotifierFiller> fillers) {
        fillerIds = new ArrayList<>();
        this.fillers = fillers;
        for(TraitNotifierFiller filler : fillers) {
            fillerIds.add(filler.getId());
        }
    }

    public TraitFiller getSelectedFiller() {
        return selectedFiller;
    }

    public void setSelectedFiller(TraitFiller selectedFiller) {
        this.selectedFiller = selectedFiller;
        this.selectedFillerId = selectedFiller.getId();
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }
}
