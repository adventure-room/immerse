package com.programyourhome.adventureroom.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.regex.MatchResult;
import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.immerse.model.StopBackgroundMusicAction;
import com.programyourhome.adventureroom.model.Adventure;

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
