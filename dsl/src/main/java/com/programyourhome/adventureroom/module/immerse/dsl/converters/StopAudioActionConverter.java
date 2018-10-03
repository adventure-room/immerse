package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import java.util.Optional;

import com.programyourhome.adventureroom.dsl.antlr.AntlrActionConverter;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.StopAudioActionContext;
import com.programyourhome.adventureroom.module.immerse.model.StopAudioAction;

public class StopAudioActionConverter implements AntlrActionConverter<StopAudioActionContext, StopAudioAction> {

    @Override
    public StopAudioAction convert(StopAudioActionContext context, Adventure adventure) {
        StopAudioAction action = new StopAudioAction();
        action.variableName = this.toString(context.variableName);
        Optional.ofNullable(context.fadeOut).ifPresent(fadeOut -> action.fadeOutMillis = Optional.of((int) (this.toDouble(fadeOut) * 1000)));
        return action;
    }

}
