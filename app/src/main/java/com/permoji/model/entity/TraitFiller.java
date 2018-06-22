package com.permoji.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by michael on 11/06/18.
 */
@Entity(tableName = "trait_filler")
public class TraitFiller implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String codepoint;
    private String text;
    private String personalisedText;
    private Integer popularityWeight;
    private String placeholderType;

    public Integer getId() {
        return id;
    }

    public String getCodepoint() {
        return codepoint;
    }

    public String getText() {
        return text;
    }

    public String getPersonalisedText() {
        return personalisedText;
    }

    public Integer getPopularityWeight() {
        return popularityWeight;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCodepoint(String codepoint) {
        this.codepoint = codepoint;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPersonalisedText(String personalisedText) {
        this.personalisedText = personalisedText;
    }

    public void setPopularityWeight(Integer popularityWeight) {
        this.popularityWeight = popularityWeight;
    }

    public String getPlaceholderType() {
        return placeholderType;
    }

    public void setPlaceholderType(String placeholderType) {
        this.placeholderType = placeholderType;
    }
}
