package com.permoji.notifications;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import javax.annotation.Nonnull;

/**
 * Created by michael on 25/05/18.
 */

@Entity(tableName = "UserNotification")
public class UserNotification {

    @Nonnull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int traitCodepoint;

    private String timeStamp;

    private String detail;

    private String traitName;

    private String imagePath;

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public int getTraitCodepoint() {
        return traitCodepoint;
    }

    public void setTraitCodepoint(int traitCodepoint) {
        this.traitCodepoint = traitCodepoint;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTraitName() { return traitName; }

    public void setTraitName(String traitName) { this.traitName = traitName; }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
