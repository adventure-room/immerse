package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import java.util.Map;

import com.programyourhome.adventureroom.dsl.regex.MatchResult;
import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.module.immerse.model.PlayBackgroundMusicAction;

public class PlayBackgroundMusicActionConverter implements RegexActionConverter<PlayBackgroundMusicAction> {

    @Override
    public Map<String, String> getRegexMap() {
        return this.createRegexes(DEFAULT, "play background music " + RESOURCE_ID);
    }

    @Override
    public PlayBackgroundMusicAction convert(MatchResult matchResult, Adventure adventure) {
        PlayBackgroundMusicAction action = new PlayBackgroundMusicAction();
        action.audioId = matchResult.getValue(RESOURCE_ID);
        return action;
    }

}
