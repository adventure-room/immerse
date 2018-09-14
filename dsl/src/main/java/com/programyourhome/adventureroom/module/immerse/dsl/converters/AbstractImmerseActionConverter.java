package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.dsl.regex.RegexVariable;
import com.programyourhome.adventureroom.model.script.action.Action;

public abstract class AbstractImmerseActionConverter<A extends Action> implements RegexActionConverter<A> {

    public static final RegexVariable SPEAKER_ID = RegexActionConverter.RESOURCE_ID.withName("speakerId");
    public static final RegexVariable SPEAKER_IDS = RegexActionConverter.RESOURCE_IDS.withName("speakerIds");
    public static final RegexVariable VOLUME = INTEGER.withName("volume");
    public static final RegexVariable SOURCE_STATIC_LOCATION = RegexActionConverter.LOCATION.withName("sourceStaticLocation");
    public static final RegexVariable SOURCE_LOCATION_PATH = RegexActionConverter.LOCATION_PATH.withName("sourceLocationPath");
    public static final RegexVariable LISTENER_STATIC_LOCATION = RegexActionConverter.LOCATION.withName("listenerStaticLocation");
    public static final RegexVariable LISTENER_LOCATION_PATH = RegexActionConverter.LOCATION_PATH.withName("listenerLocationPath");
    public static final RegexVariable SOURCE_PATH_SPEED = DOUBLE.withName("sourcePathSpeed");
    public static final RegexVariable LISTENER_PATH_SPEED = DOUBLE.withName("listenerPathSpeed");
    public static final RegexVariable REPEAT = INTEGER.withName("repeat");

    public String optionalAtVolume() {
        return this.optional(" at volume " + VOLUME);
    }

}
