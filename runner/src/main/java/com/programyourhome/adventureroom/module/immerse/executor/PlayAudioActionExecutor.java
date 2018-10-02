package com.programyourhome.adventureroom.module.immerse.executor;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.toolbox.ContentCategory;
import com.programyourhome.adventureroom.model.toolbox.DataStream;
import com.programyourhome.adventureroom.model.util.StreamUtil;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction;
import com.programyourhome.adventureroom.module.immerse.service.ScenarioBuilder;
import com.programyourhome.immerse.domain.Factory;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;
import com.programyourhome.immerse.domain.location.dynamic.DynamicLocation;

public class PlayAudioActionExecutor extends AbstractImmerseExecutor<PlayAudioAction> {

    @Override
    public void execute(PlayAudioAction action, ExecutionContext context) {
        ScenarioBuilder builder = this.getImmerse(context).scenarioBuilder()
                .name(action.resource.toString())
                .description("Audio '" + action.resource.toString() + "' triggered by the Immerse Adventure Module");

        action.resource.getFilename().ifPresent(filename -> {
            DataStream dataStream = context.getToolbox().getContentService().getContent(ContentCategory.AUDIO, filename);
            URL url = context.getToolbox().getDataStreamToUrl().exposeDataStream(dataStream);
            // TODO: for now hardcoded wav for all files
            builder.urlWithType(url.toString(), AudioFileType.WAVE);
        });

        action.resource.getUrl().ifPresent(urlResource -> {
            // TODO: can this isPresent/get be avoided?
            if (urlResource.audioFormat.isPresent()) {
                builder.urlWithFormat(urlResource.urlString, urlResource.audioFormat.get());
            } else {
                // TODO: for now hardcoded wav for all url's without format
                builder.urlWithType(urlResource.urlString, AudioFileType.WAVE);
            }
        });

        action.volume.ifPresent(volume -> {
            double fractionalVolume = volume.volumePercentage / 100;
            builder.volume(fractionalVolume);
            volume.fadeInMillis.ifPresent(fadeInMillis -> builder.linearVolume(0, fractionalVolume, fadeInMillis));
        });

        action.soundSource.ifPresent(soundSource -> {
            soundSource.getSpeakerIds().ifPresent(builder::sourceAtSpeakers);
            soundSource.getDynamicLocation().ifPresent(sourceDynamicLocation -> {
                Factory<DynamicLocation> sourceLocation = this.toImmerseDynamicLocation(sourceDynamicLocation, builder);
                Factory<DynamicLocation> listenerLocation = action.listenerLocation
                        .map(listenerDynamicLocation -> this.toImmerseDynamicLocation(listenerDynamicLocation, builder))
                        .orElse(builder.atCenter());
                builder.fieldOfHearingVolume(sourceLocation, listenerLocation);
                builder.volumeAsOneSpeaker();
            });
        });

        action.normalize.ifPresent(normalize -> {
            normalize.getAsOneSpeaker().ifPresent(one -> builder.volumeAsOneSpeaker());
            normalize.getAsAllSpeakers().ifPresent(all -> builder.normalizeVolume());
        });

        action.playback.ifPresent(playback -> {
            playback.getOnce().ifPresent(once -> builder.playOnce());
            playback.getRepeat().ifPresent(repeat -> builder.playRepeat(repeat));
            playback.getForever().ifPresent(forever -> builder.playRepeatForever());
            playback.getSeconds().ifPresent(seconds -> builder.playForDuration(Duration.ofSeconds(seconds)));
        });

        Scenario scenario = builder.build();
        UUID playbackID = this.getImmerse(context).playScenario(scenario);
        this.getImmerse(context).waitForPlayback(playbackID);
    }

    private Factory<DynamicLocation> toImmerseDynamicLocation(
            com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.DynamicLocation dynamicLocation, ScenarioBuilder builder) {
        return StreamUtil.getOne(
                dynamicLocation.getStaticLocation().map(builder::atLocation),
                dynamicLocation.getPath().map(path -> builder.atPath(path.waypoints, path.speed, false)),
                dynamicLocation.getCircling().map(circling -> builder.circling(circling.center, circling.startAngle.orElse(0D), circling.radius,
                        circling.speed, circling.clockwise.orElse(true))));
    }

}
