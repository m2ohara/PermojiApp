package com.permoji.notifications;

import android.graphics.drawable.Drawable;

/**
 * Created by michael on 25/05/18.
 */

public class Notification {

    private int id;

    private Drawable notifierImage;

    private int traitCodepoint;

    private String time;

    private String detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTraitCodepoint() {
        return traitCodepoint;
    }

    public void setTraitCodepoint(int traitCodepoint) {
        this.traitCodepoint = traitCodepoint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
