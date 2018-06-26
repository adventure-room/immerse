package com.programyourhome.adventureroom.module.immerse.model;

import com.programyourhome.adventureroom.model.resource.ExternalResource;
import com.programyourhome.immerse.domain.speakers.Speaker;

public class SpeakerExternalResource implements ExternalResource<Speaker> {

    private final Speaker speaker;

    public SpeakerExternalResource(Speaker speaker) {
        this.speaker = speaker;
    }

    @Override
    public String getId() {
        return "" + this.speaker.getId();
    }

    @Override
    public String getName() {
        return this.speaker.getName();
    }

    @Override
    public String getDescription() {
        return this.speaker.getDescription();
    }

    @Override
    public Class<Speaker> getWrappedObjectClass() {
        return Speaker.class;
    }

    @Override
    public Speaker getWrappedObject() {
        return this.speaker;
    }

}
