package com.permoji.model.result;

import android.arch.persistence.room.Relation;

import com.permoji.model.Notifier;
import com.permoji.model.NotifierFiller;
import com.permoji.model.TraitFiller;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michael on 15/06/18.
 */

public class TraitNotifierFillerResult implements Serializable {

    private int id;
    private int traitDefinitionId;
    private int notifierId;

    @Relation(parentColumn = "id", entityColumn = "traitNotifierFillerId", entity = NotifierFiller.class)
    public List<NotifierFillerResult> notifierFillerResultList;
    @Relation(parentColumn = "notifierId", entityColumn = "id", entity = Notifier.class)
    public List<Notifier> notifier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTraitDefinitionId() {
        return traitDefinitionId;
    }

    public void setTraitDefinitionId(int traitDefinitionId) {
        this.traitDefinitionId = traitDefinitionId;
    }

    public int getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(int notifierId) {
        this.notifierId = notifierId;
    }
}

