package com.permoji.model.result;

import android.arch.persistence.room.Relation;

import com.permoji.model.entity.TraitFiller;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michael on 21/06/18.
 */

public class NotifierFillerResult implements Serializable {

    private int id;
    private int traitNotifierFillerId;
    private int fillerId;

    @Relation(parentColumn = "fillerId", entityColumn = "id", entity = TraitFiller.class)
    public List<TraitFiller> traitFiller;

    public int getTraitNotifierFillerId() {
        return traitNotifierFillerId;
    }

    public void setTraitNotifierFillerId(int traitNotifierFillerId) {
        this.traitNotifierFillerId = traitNotifierFillerId;
    }

    public int getFillerId() {
        return fillerId;
    }

    public void setFillerId(int fillerId) {
        this.fillerId = fillerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TraitFiller getNotifierFiller() {
        return traitFiller.get(0);
    }
}
