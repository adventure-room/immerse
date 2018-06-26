package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.regex.MatchResult;
import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.module.immerse.model.StopBackgroundMusicAction;

public class StopBackgroundMusicActionConverter implements RegexActionConverter<StopBackgroundMusicAction> {

    @Override
    public String getRegexLine() {
        return "stop background music";
    }

    @Override
    public StopBackgroundMusicAction convert(MatchResult matchResult, Adventure adventure) {
        return new StopBackgroundMusicAction();
    }

}
