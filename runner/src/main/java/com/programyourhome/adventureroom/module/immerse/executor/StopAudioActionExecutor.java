package com.programyourhome.adventureroom.module.immerse.executor;

import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.util.IOUtil;
import com.programyourhome.adventureroom.module.immerse.model.StopAudioAction;

public class StopAudioActionExecutor extends AbstractImmerseExecutor<StopAudioAction> {

    private UUID playbackId;

    @Override
    public void execute(StopAudioAction action, ExecutionContext context) {
        if (!context.isVariableDefined(action.variableName)) {
            throw new IllegalStateException("There is audio playing with variable " + action.variableName);
        }
        this.playbackId = context.getVariableValue(action.variableName);
        if (action.fadeOutMillis.isPresent()) {
            this.getImmerse(context).fadeOutPlayback(this.playbackId, action.fadeOutMillis.get());
        } else {
            this.getImmerse(context).stopPlayback(this.playbackId);
        }
    }

    @Override
    public void stop(ExecutionContext context) {
        // Force stop here as well, cause main activity might be stuck in fade out.
        IOUtil.waitForCondition(() -> this.playbackId != null);
        this.getImmerse(context).stopPlayback(this.playbackId);
    }

}
