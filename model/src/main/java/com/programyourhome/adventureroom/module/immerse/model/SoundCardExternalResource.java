package com.programyourhome.adventureroom.module.immerse.model;

import com.programyourhome.adventureroom.model.resource.AbstractExternalResource;
import com.programyourhome.immerse.domain.audio.soundcard.SoundCard;

public class SoundCardExternalResource extends AbstractExternalResource<SoundCard> {

    public SoundCardExternalResource(SoundCard soundCard) {
        super(soundCard);
    }

    public SoundCard getSoundCard() {
        return this.getWrappedObject();
    }

    @Override
    public String getId() {
        return "" + this.getSoundCard().getId();
    }

    @Override
    public String getName() {
        return this.getSoundCard().getName();
    }

    @Override
    public String getDescription() {
        return this.getSoundCard().getDescription();
    }

}
