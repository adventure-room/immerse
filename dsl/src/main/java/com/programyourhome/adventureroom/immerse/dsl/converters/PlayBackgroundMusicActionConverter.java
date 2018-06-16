package com.programyourhome.adventureroom.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.regex.MatchResult;
import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.immerse.model.PlayBackgroundMusicAction;
import com.programyourhome.adventureroom.model.Adventure;

public class PlayBackgroundMusicActionConverter implements RegexActionConverter<PlayBackgroundMusicAction> {

    @Override
    public PlayBackgroundMusicAction convert(MatchResult matchResult, Adventure adventure) {
        PlayBackgroundMusicAction action = new PlayBackgroundMusicAction();
        action.audioId = matchResult.getValue("id");
        return action;
    }

}
