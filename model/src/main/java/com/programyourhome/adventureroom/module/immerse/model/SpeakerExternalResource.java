package com.programyourhome.adventureroom.module.immerse.model;

import com.programyourhome.adventureroom.model.resource.AbstractExternalResource;
import com.programyourhome.immerse.domain.speakers.Speaker;

public class SpeakerExternalResource extends AbstractExternalResource<Speaker> {

    public SpeakerExternalResource(Speaker speaker) {
        super(speaker);
    }

    public Speaker getSpeaker() {
        return this.getWrappedObject();
    }

    @Override
    public String getId() {
        return "" + this.getSpeaker().getId();
    }

    @Override
    public String getName() {
        return this.getSpeaker().getName();
    }

    @Override
    public String getDescription() {
        return this.getSpeaker().getDescription();
    }

}
