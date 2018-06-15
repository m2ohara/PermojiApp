package com.permoji.trait.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by michael on 12/06/18.
 */
@Entity(tableName = "trait_notifier_filler", foreignKeys={
        @ForeignKey(
                entity=TraitFiller.class,
                parentColumns="id",
                childColumns="fillerId"),
        @ForeignKey(
                entity=Notifier.class,
                parentColumns="id",
                childColumns="notifierId")},
        indices=@Index(value="fillerId"))
public class TraitNotifierFiller {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int traitDefinitionId;
    private int notifierId;
    private int fillerId;


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

    public int getFillerId() {
        return fillerId;
    }

    public void setFillerId(int fillerId) {
        this.fillerId = fillerId;
    }
}
