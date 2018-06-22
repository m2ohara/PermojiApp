package com.permoji.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by michael on 21/06/18.
 */

@Entity(tableName = "trait_adjust_properties")
public class TraitAdjustProperties {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
