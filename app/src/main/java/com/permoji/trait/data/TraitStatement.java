package com.permoji.trait.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by michael on 11/06/18.
 */
@Entity(tableName = "trait_statement")
public class TraitStatement
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codepoint;
    private String traitName;
    private String statement;
    private int popularityWeight;

    public int getId() {
        return id;
    }

    public String getCodepoint() {
        return codepoint;
    }

    public void setCodepoint(String codepoint) {
        this.codepoint = codepoint;
    }

    public int getCodePoint() {
        String result = codepoint.replace("U+", "");
        return  result.codePointAt(0);
    }

    public String getTraitName() {
        return traitName;
    }

    public String getStatement() {
        return statement;
    }

    public int getPopularityWeight() {
        return popularityWeight;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setPopularityWeight(int popularityWeight) {
        this.popularityWeight = popularityWeight;
    }
}
