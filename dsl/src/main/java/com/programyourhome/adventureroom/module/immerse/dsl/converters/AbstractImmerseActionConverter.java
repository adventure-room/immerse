package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.dsl.regex.RegexVariable;
import com.programyourhome.adventureroom.model.script.action.Action;

public abstract class AbstractImmerseActionConverter<A extends Action> implements RegexActionConverter<A> {

    public static final RegexVariable SPEAKER_ID = RegexActionConverter.RESOURCE_ID.withName("speakerId");
    public static final RegexVariable SPEAKER_IDS = RegexActionConverter.RESOURCE_IDS.withName("speakerIds");
    public static final RegexVariable VOLUME = DOUBLE.withName("volume");

    public String optionalAtVolume() {
        return this.optional(" at volume " + VOLUME);
    }

}
