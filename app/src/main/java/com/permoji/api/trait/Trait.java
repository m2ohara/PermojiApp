package com.permoji.api.trait;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by michael on 22/05/18.
 */

@Entity(tableName = "trait")
public class Trait {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @Nonnull
    private Integer id;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("codepoint")
    @Expose
    private int codepoint;
    @SerializedName("imageNames")
    @Expose
    private ArrayList<String> voucherImageNames;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCodepoint() {
        return codepoint;
    }

    public void setCodepoint(int codepoint) {
        this.codepoint = codepoint;
    }

    public ArrayList<String> getVoucherImageNames() {
        return voucherImageNames;
    }

    public void setVoucherImageNames(ArrayList<String> voucherImageNames) {
        this.voucherImageNames = voucherImageNames;
    }
}
