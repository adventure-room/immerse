package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import java.util.Optional;

import com.programyourhome.adventureroom.dsl.antlr.AntlrActionConverter;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.StopBackgroundMusicActionContext;
import com.programyourhome.adventureroom.module.immerse.model.StopBackgroundMusicAction;

public class StopBackgroundMusicActionConverter implements AntlrActionConverter<StopBackgroundMusicActionContext, StopBackgroundMusicAction> {

    @Override
    public StopBackgroundMusicAction convert(StopBackgroundMusicActionContext context, Adventure adventure) {
        StopBackgroundMusicAction action = new StopBackgroundMusicAction();
        Optional.ofNullable(context.fadeOut).ifPresent(fadeOut -> action.fadeOutMillis = Optional.of((int) (this.toDouble(fadeOut) * 1000)));
        return action;
    }

}
