package com.permoji.trait;

import android.arch.persistence.room.Entity;

/**
 * Created by michael on 11/06/18.
 */
@Entity
public class TraitStatement
{
    private int id;
    private int codepoint;
    private String traitname;
    private String statement;
    private int popularityWeight;

    public int getId() {
        return id;
    }

    public int getCodepoint() {
        return codepoint;
    }

    public String getTraitname() {
        return traitname;
    }

    public String getStatement() {
        return statement;
    }

    public int getPopularityWeight() {
        return popularityWeight;
    }
}
