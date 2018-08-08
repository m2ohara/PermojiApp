package com.permoji.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by michael on 12/06/18.
 */
@Entity(
        tableName = "trait_notifier_filler",
        foreignKeys = {
        @ForeignKey(
                entity = Trait.class,
                parentColumns = "id",
                childColumns = "traitDefinitionId",
                onDelete = ForeignKey.CASCADE
        )},
        indices = { @Index(value = "traitDefinitionId")}
)
public class TraitNotifierFiller {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer traitDefinitionId;
    private Integer notifierId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTraitDefinitionId() {
        return traitDefinitionId;
    }

    public void setTraitDefinitionId(Integer traitDefinitionId) {
        this.traitDefinitionId = traitDefinitionId;
    }

    public Integer getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(Integer notifierId) {
        this.notifierId = notifierId;
    }
}
