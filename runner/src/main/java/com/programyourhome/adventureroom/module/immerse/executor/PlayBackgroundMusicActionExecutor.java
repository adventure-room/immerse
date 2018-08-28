package com.programyourhome.adventureroom.module.immerse.executor;

import java.util.UUID;

import com.programyourhome.adventureroom.module.immerse.model.PlayBackgroundMusicAction;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;
import com.programyourhome.iotadventure.runner.context.ExecutionContext;

public class PlayBackgroundMusicActionExecutor extends AbstractImmerseExecutor<PlayBackgroundMusicAction> {

    public static final String BACKGROUND_MUSIC_VARIABLE_NAME = "immerse.background.music";

    @Override
    public void execute(PlayBackgroundMusicAction action, ExecutionContext context) {
        if (context.isVariableDefined(BACKGROUND_MUSIC_VARIABLE_NAME)) {
            throw new IllegalStateException("There is already background music playing");
        }
        UUID playbackID = this.getImmerse(context).playAtSpeakers("http://localhost:19161/audio/" + action.audioId, AudioFileType.WAVE,
                this.getSpeakerIds(context), true, false);
        context.setVariableValue(BACKGROUND_MUSIC_VARIABLE_NAME, playbackID);
    }

}
