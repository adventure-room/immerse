package com.programyourhome.adventureroom.module.immerse.executor;

import static com.programyourhome.adventureroom.module.immerse.executor.PlayBackgroundMusicActionExecutor.BACKGROUND_MUSIC_VARIABLE_NAME;

import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.util.IOUtil;
import com.programyourhome.adventureroom.module.immerse.model.StopBackgroundMusicAction;

public class StopBackgroundMusicActionExecutor extends AbstractImmerseExecutor<StopBackgroundMusicAction> {

    private UUID playbackId;

    @Override
    public void execute(StopBackgroundMusicAction action, ExecutionContext context) {
        if (!context.isVariableDefined(BACKGROUND_MUSIC_VARIABLE_NAME)) {
            throw new IllegalStateException("There is no background music playing");
        }
        this.playbackId = context.getVariableValue(PlayBackgroundMusicActionExecutor.BACKGROUND_MUSIC_VARIABLE_NAME);
        if (action.fadeOutMillis.isPresent()) {
            this.getImmerse(context).fadeOutPlayback(this.playbackId, action.fadeOutMillis.get());
            // TODO: wait for fade out
        } else {
            this.getImmerse(context).stopPlayback(this.playbackId);
        }
        // TODO: maybe better just wait for also after stop, so the playback is really gone, either way
        context.removeVariable(BACKGROUND_MUSIC_VARIABLE_NAME);
    }

    @Override
    public void stop(ExecutionContext context) {
        // Force stop here as well, cause main activity might be stuck in fade out.
        IOUtil.waitForCondition(() -> this.playbackId != null);
        this.getImmerse(context).stopPlayback(this.playbackId);
    }

}
