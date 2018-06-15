package com.permoji.trait.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by michael on 11/06/18.
 */
@Entity(tableName = "trait_filler")
public class TraitFiller {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codepoint;
    private String text;
    private String personalisedText;
    private int popularityWeight;

    public int getId() {
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

    public int getPopularityWeight() {
        return popularityWeight;
    }

    public void setId(int id) {
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

    public void setPopularityWeight(int popularityWeight) {
        this.popularityWeight = popularityWeight;
    }
}
