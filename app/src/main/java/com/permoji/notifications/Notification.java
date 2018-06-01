package com.permoji.notifications;

import android.graphics.drawable.Drawable;

/**
 * Created by michael on 25/05/18.
 */

public class Notification {

    private int id;

    private Drawable notifierImage;

    private int traitCodepoint;

    private String timePassed;

    private String detail;

    private String traitName;

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

    public String getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(String timePassed) {
        this.timePassed = timePassed;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Drawable getNotifierImage() {
        return notifierImage;
    }

    public void setNotifierImage(Drawable notifierImage) {
        this.notifierImage = notifierImage;
    }

    public String getTraitName() { return traitName; }

    public void setTraitName(String traitName) { this.traitName = traitName; }
}
