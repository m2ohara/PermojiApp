package com.permoji.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by michael on 20/06/18.
 */

@Entity(tableName = "notifier_filler", foreignKeys = {
        @ForeignKey(
                entity=TraitFiller.class,
                parentColumns="id",
                childColumns="fillerId"),
        @ForeignKey(
                entity=TraitNotifierFiller.class,
                parentColumns="id",
                childColumns="traitNotifierFillerId")},
        indices={@Index(value="fillerId"),@Index(value = "traitNotifierFillerId")})
public class NotifierFiller implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int traitNotifierFillerId;
    private int fillerId;

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
}
