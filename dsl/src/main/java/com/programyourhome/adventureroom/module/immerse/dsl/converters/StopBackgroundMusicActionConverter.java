package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.antlr.AntlrActionConverter;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.StopBackgroundMusicActionContext;
import com.programyourhome.adventureroom.module.immerse.model.StopBackgroundMusicAction;

public class StopBackgroundMusicActionConverter implements AntlrActionConverter<StopBackgroundMusicActionContext, StopBackgroundMusicAction> {

    @Override
    public StopBackgroundMusicAction convert(StopBackgroundMusicActionContext context, Adventure adventure) {
        return new StopBackgroundMusicAction();
    }

}
