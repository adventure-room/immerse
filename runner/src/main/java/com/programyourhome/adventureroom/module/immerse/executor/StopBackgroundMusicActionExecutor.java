package com.programyourhome.adventureroom.module.immerse.executor;

import static com.programyourhome.adventureroom.module.immerse.executor.PlayBackgroundMusicActionExecutor.BACKGROUND_MUSIC_VARIABLE_NAME;

import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.module.immerse.model.StopBackgroundMusicAction;

public class StopBackgroundMusicActionExecutor extends AbstractImmerseExecutor<StopBackgroundMusicAction> {

    @Override
    public void execute(StopBackgroundMusicAction action, ExecutionContext context) {
        if (!context.isVariableDefined(BACKGROUND_MUSIC_VARIABLE_NAME)) {
            throw new IllegalStateException("There is no background music playing");
        }
        UUID playbackId = context.getVariableValue(PlayBackgroundMusicActionExecutor.BACKGROUND_MUSIC_VARIABLE_NAME);
        if (action.fadeOutMillis.isPresent()) {
            this.getImmerse(context).fadeOutPlayback(playbackId, action.fadeOutMillis.get());
        } else {
            this.getImmerse(context).stopPlayback(playbackId);
        }
        context.removeVariable(BACKGROUND_MUSIC_VARIABLE_NAME);
    }

}
