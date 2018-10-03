package com.programyourhome.adventureroom.module.immerse.executor;

import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.module.immerse.model.StopAudioAction;

public class StopAudioActionExecutor extends AbstractImmerseExecutor<StopAudioAction> {

    @Override
    public void execute(StopAudioAction action, ExecutionContext context) {
        if (!context.isVariableDefined(action.variableName)) {
            throw new IllegalStateException("There is audio playing with variable " + action.variableName);
        }
        UUID playbackId = context.getVariableValue(action.variableName);
        if (action.fadeOutMillis.isPresent()) {
            this.getImmerse(context).fadeOutPlayback(playbackId, action.fadeOutMillis.get());
        } else {
            this.getImmerse(context).stopPlayback(playbackId);
        }
    }

}
