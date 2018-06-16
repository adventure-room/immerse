package com.programyourhome.adventureroom.immerse.executor;

import com.programyourhome.adventureroom.immerse.model.PlayBackgroundMusicAction;
import com.programyourhome.iotadventure.runner.context.ExecutionContext;

public class PlayBackgroundMusicActionExecutor extends AbstractImmerseExecutor<PlayBackgroundMusicAction> {

    @Override
    public void execute(PlayBackgroundMusicAction action, ExecutionContext context) {
        this.getImmerse(context).playBackgroundMusic(action.audioId);
    }

}
