package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import java.util.Map;

import com.programyourhome.adventureroom.dsl.regex.MatchResult;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.module.immerse.model.PlayBackgroundMusicAction;

public class PlayBackgroundMusicActionConverter extends AbstractImmerseActionConverter<PlayBackgroundMusicAction> {

    // TODO: make parameter for this
    public static final double DEFAULT_BACKGROUND_MUSIC_VOLUME = 0.3;

    @Override
    public Map<String, String> getRegexMap() {
        return this.createRegexes(DEFAULT, "play background music " + FILENAME + this.optionalAtVolume());
    }

    @Override
    public PlayBackgroundMusicAction convert(MatchResult matchResult, Adventure adventure) {
        PlayBackgroundMusicAction action = new PlayBackgroundMusicAction();
        action.filename = matchResult.getValue(FILENAME);
        action.volume = matchResult.getOptionalValue(VOLUME).map(Double::valueOf);
        return action;
    }

}
