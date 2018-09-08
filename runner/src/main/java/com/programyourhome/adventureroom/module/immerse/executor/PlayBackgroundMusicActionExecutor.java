package com.programyourhome.adventureroom.module.immerse.executor;

import java.net.URL;
import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.toolbox.ContentCategory;
import com.programyourhome.adventureroom.model.toolbox.DataStream;
import com.programyourhome.adventureroom.module.immerse.model.PlayBackgroundMusicAction;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;

//TODO: merge building the scenario logic with PlayAudioActionExecutor
public class PlayBackgroundMusicActionExecutor extends AbstractImmerseExecutor<PlayBackgroundMusicAction> {

    public static final String BACKGROUND_MUSIC_VARIABLE_NAME = "immerse.background.music";

    @Override
    public void execute(PlayBackgroundMusicAction action, ExecutionContext context) {
        if (context.isVariableDefined(BACKGROUND_MUSIC_VARIABLE_NAME)) {
            throw new IllegalStateException("There is already background music playing");
        }
        DataStream dataStream = context.getToolbox().getContentService().getContent(ContentCategory.AUDIO, action.filename);
        URL url = context.getToolbox().getDataStreamToUrl().exposeDataStream(dataStream);
        Scenario scenario = this.getImmerse(context).scenarioBuilder()
                .name("Background Music")
                .description("Background music '" + action.filename + "' triggered by the Immerse Adventure Module")
                .urlWithType(url.toString(), AudioFileType.WAVE)
                // Background music, so not full volume.
                .sourceAtAllSpeakers(0.3)
                .playRepeatForever()
                .build();
        UUID playbackID = this.getImmerse(context).playScenario(scenario);
        context.setVariableValue(BACKGROUND_MUSIC_VARIABLE_NAME, playbackID);
    }

}
