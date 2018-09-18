package com.programyourhome.adventureroom.module.immerse.executor;

import java.net.URL;
import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.toolbox.ContentCategory;
import com.programyourhome.adventureroom.model.toolbox.DataStream;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction;
import com.programyourhome.adventureroom.module.immerse.service.ScenarioBuilder;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;

public class PlayAudioActionExecutor extends AbstractImmerseExecutor<PlayAudioAction> {

    @Override
    public void execute(PlayAudioAction action, ExecutionContext context) {
        DataStream dataStream = context.getToolbox().getContentService().getContent(ContentCategory.AUDIO, action.filename);
        URL url = context.getToolbox().getDataStreamToUrl().exposeDataStream(dataStream);
        ScenarioBuilder builder = this.getImmerse(context).scenarioBuilder()
                .name("Audio")
                .description("Audio '" + action.filename + "' triggered by the Immerse Adventure Module")
                .urlWithType(url.toString(), AudioFileType.WAVE);

        action.volume.ifPresent(volumePercentage -> builder.volume(volumePercentage / 100.0));

        // TODO: etc etc etc
        action.listenerLocation.ifPresent(listenerLocation -> {
            listenerLocation.getStaticLocation().ifPresent(builder::listenerAtLocation);
            listenerLocation.getPath().ifPresent(path -> builder.listenerAtPath(path.waypoints, path.speed, true));
        });

        // builder.sourceAtSpeakers(action.speakerIds.orElse(this.getImmerse(context).getSettings().getRoom().getSpeakers().keySet()));
        // action.sourceLocation.ifPresent(builder::sourceAtLocation);

        Scenario scenario = builder
                .playOnce()
                .build();
        UUID playbackID = this.getImmerse(context).playScenario(scenario);
        this.getImmerse(context).waitForPlayback(playbackID);
    }

}
