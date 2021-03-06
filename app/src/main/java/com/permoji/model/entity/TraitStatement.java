package com.permoji.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by michael on 11/06/18.
 */
@Entity(tableName = "trait_statement")
public class TraitStatement implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String codepoint;
    private String traitName;
    private String statement;
    private Integer popularityWeight;
    private String heading;
    private Integer personaliseWeight;
    private String placeholderType;

    public Integer getId() {
        return id;
    }

    public String getCodepoint() {
        return codepoint;
    }

    public void setCodepoint(String codepoint) {
        this.codepoint = codepoint;
    }

    public Integer getCodePoint() {
        Integer result=Integer.parseInt(codepoint.substring(2),16);
        return  result;
    }

    public String getTraitName() {
        return traitName;
    }

    public String getStatement() {
        return statement;
    }

    public Integer getPopularityWeight() {
        return popularityWeight;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setTraitName(String traitName) {
        this.traitName = traitName;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setPopularityWeight(Integer popularityWeight) {
        this.popularityWeight = popularityWeight;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public Integer getPersonaliseWeight() {
        return personaliseWeight;
    }

    public void setPersonaliseWeight(Integer personaliseWeight) {
        this.personaliseWeight = personaliseWeight;
    }

    public String getPlaceholderType() {
        return placeholderType;
    }

    public void setPlaceholderType(String placeholderType) {
        this.placeholderType = placeholderType;
    }
}
