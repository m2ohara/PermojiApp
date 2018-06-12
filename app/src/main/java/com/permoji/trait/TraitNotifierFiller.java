package com.permoji.trait;

/**
 * Created by michael on 12/06/18.
 */

public class TraitNotifierFiller {

    private int id;
    private int notifierId;
    private int fillerId;

    private TraitFiller traitFiller;
    private Notifier notifier;

    public TraitFiller getTraitFiller() {
        return traitFiller;
    }

    public void setTraitFiller(TraitFiller traitFiller) {
        this.traitFiller = traitFiller;
        this.fillerId = traitFiller.getId();
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
        this.notifierId = notifier.getId();
    }

    public int getId() {
        return id;
    }
}
