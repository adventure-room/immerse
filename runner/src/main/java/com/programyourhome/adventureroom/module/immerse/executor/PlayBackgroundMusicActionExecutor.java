package com.programyourhome.adventureroom.module.immerse.executor;

import java.net.URL;
import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.toolbox.ContentCategory;
import com.programyourhome.adventureroom.model.toolbox.DataStream;
import com.programyourhome.adventureroom.model.util.IOUtil;
import com.programyourhome.adventureroom.module.immerse.model.PlayBackgroundMusicAction;
import com.programyourhome.adventureroom.module.immerse.service.ScenarioBuilder;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;

//TODO: merge building the scenario logic with PlayAudioActionExecutor
public class PlayBackgroundMusicActionExecutor extends AbstractImmerseExecutor<PlayBackgroundMusicAction> {

    public static final String BACKGROUND_MUSIC_VARIABLE_NAME = "immerse.background.music";

    private UUID playbackId;

    @Override
    public void execute(PlayBackgroundMusicAction action, ExecutionContext context) {
        if (context.isVariableDefined(BACKGROUND_MUSIC_VARIABLE_NAME)) {
            throw new IllegalStateException("There is already background music playing");
        }
        DataStream dataStream = context.getToolbox().getContentService().getContent(ContentCategory.AUDIO, action.filename);
        URL url = context.getToolbox().getDataStreamToUrl().exposeDataStream(dataStream);
        ScenarioBuilder builder = this.getImmerse(context).scenarioBuilder()
                .name("Background Music")
                .description("Background music '" + action.filename + "' triggered by the Immerse Adventure Module")
                .urlWithType(url.toString(), AudioFileType.WAVE);

        // TODO: make a property out of default setting
        // Background music, so not full volume.
        // Set the default, which can be overridden below.
        builder.volume(0.3);

        action.volume.ifPresent(volume -> {
            double fractionalVolume = volume.volumePercentage / 100.0;
            builder.volume(fractionalVolume);
            volume.fadeInMillis.ifPresent(fadeInMillis -> builder.linearVolume(0, fractionalVolume, fadeInMillis));
        });

        Scenario scenario = builder
                .sourceAtAllSpeakers()
                .playRepeatForever()
                .build();
        this.playbackId = this.getImmerse(context).playScenario(scenario);
        context.setVariableValue(BACKGROUND_MUSIC_VARIABLE_NAME, this.playbackId);
    }

    @Override
    public void stop(ExecutionContext context) {
        IOUtil.waitForCondition(() -> this.playbackId != null);
        this.getImmerse(context).stopPlayback(this.playbackId);
    }

}
