package com.programyourhome.adventureroom.module.immerse.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.ToDoubleFunction;

import org.la4j.Vectors;

import com.programyourhome.immerse.domain.Factory;
import com.programyourhome.immerse.domain.ImmerseSettings;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.ScenarioSettings;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;
import com.programyourhome.immerse.domain.location.Vector3D;
import com.programyourhome.immerse.domain.location.dynamic.DynamicLocation;
import com.programyourhome.immerse.domain.speakers.Speaker;
import com.programyourhome.immerse.domain.speakers.SpeakerVolumeRatios;
import com.programyourhome.immerse.toolbox.audio.playback.ForeverPlayback;
import com.programyourhome.immerse.toolbox.audio.playback.LoopPlayback;
import com.programyourhome.immerse.toolbox.audio.playback.TimerPlayback;
import com.programyourhome.immerse.toolbox.audio.resource.FileAudioResource;
import com.programyourhome.immerse.toolbox.audio.resource.UrlAudioResource;
import com.programyourhome.immerse.toolbox.location.dynamic.FixedDynamicLocation;
import com.programyourhome.immerse.toolbox.location.dynamic.HorizontalCircleDynamicLocation;
import com.programyourhome.immerse.toolbox.location.dynamic.KeyFramesDynamicLocation;
import com.programyourhome.immerse.toolbox.speakers.algorithms.normalize.FractionalNormalizeAlgorithm;
import com.programyourhome.immerse.toolbox.speakers.algorithms.normalize.MaxSumNormalizeAlgorithm;
import com.programyourhome.immerse.toolbox.speakers.algorithms.volumeratios.FieldOfHearingVolumeRatiosAlgorithm;
import com.programyourhome.immerse.toolbox.speakers.algorithms.volumeratios.FixedVolumeRatiosAlgorithm;

import one.util.streamex.StreamEx;

public class ScenarioBuilderImpl implements ScenarioBuilder {

    private final ImmerseSettings immerseSettings;
    private final Scenario.Builder immerseScenarioBuilder;
    private final ScenarioSettings.Builder immerseScenarioSettingsBuilder;

    public ScenarioBuilderImpl(ImmerseSettings immerseSettings) {
        this.immerseSettings = immerseSettings;
        this.immerseScenarioBuilder = Scenario.builder()
                .name("Builder Scenario")
                .description("Scenario built by the ScenarioBuilder");
        this.immerseScenarioSettingsBuilder = ScenarioSettings.builder();
        this.sourceAtAllSpeakers();
        this.listenerAtCenter();
        this.fieldOfHearingVolume();
        this.volumeAsOneSpeaker();
        this.playOnce();
    }

    @Override
    public ScenarioBuilder name(String name) {
        this.immerseScenarioBuilder.name(name);
        return this;
    }

    @Override
    public ScenarioBuilder description(String name) {
        this.immerseScenarioBuilder.description(name);
        return this;
    }

    @Override
    public ScenarioBuilder file(String path) {
        this.immerseScenarioSettingsBuilder.audioResource(FileAudioResource.file(path));
        return this;
    }

    @Override
    public ScenarioBuilder urlWithFormat(String url, ImmerseAudioFormat format) {
        this.immerseScenarioSettingsBuilder.audioResource(UrlAudioResource.urlWithFormat(url, format));
        return this;
    }

    @Override
    public ScenarioBuilder urlWithType(String url, AudioFileType type) {
        this.immerseScenarioSettingsBuilder.audioResource(UrlAudioResource.urlWithType(url, type));
        return this;
    }

    @Override
    public ScenarioBuilder sourceAtSpeaker(int speakerId) {
        return this.sourceAtSpeaker(speakerId, 1);
    }

    @Override
    public ScenarioBuilder sourceAtSpeaker(int speakerId, double volume) {
        return this.sourceAtSpeakers(Arrays.asList(speakerId), volume);
    }

    @Override
    public ScenarioBuilder sourceAtSpeakers(Collection<Integer> speakerIds) {
        return this.sourceAtSpeakers(speakerIds, 1);
    }

    @Override
    public ScenarioBuilder sourceAtSpeakers(Collection<Integer> speakerIds, double volume) {
        // Source location does not matter with fixed speaker volumes, so just set to the center of the room.List
        this.immerseScenarioSettingsBuilder.sourceLocation(this.calculateCenterOfRoom());
        return this.fixedVolumesAbsolute(this.volumeAtSpeakers(speakerIds, volume));
    }

    @Override
    public ScenarioBuilder sourceAtAllSpeakers() {
        return this.sourceAtAllSpeakers(1);
    }

    @Override
    public ScenarioBuilder sourceAtAllSpeakers(double volume) {
        return this.sourceAtSpeakers(this.immerseSettings.getRoom().getSpeakers().keySet(), volume);
    }

    @Override
    public ScenarioBuilder sourceAtLocation(Vector3D location) {
        this.immerseScenarioSettingsBuilder.sourceLocation(FixedDynamicLocation.fixed(location));
        return this;
    }

    @Override
    // TODO: unit test this!
    public ScenarioBuilder sourceAtPath(List<Vector3D> path, double unitsPerSecond, boolean loop) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("path cannot be empty");
        }
        SortedMap<Long, Vector3D> keyFrames = new TreeMap<>();
        double travelTimeSoFar = 0;
        keyFrames.put(Math.round(travelTimeSoFar), path.get(0));
        for (int i = 1; i < path.size(); i++) {
            Vector3D previousPoint = path.get(i - 1);
            Vector3D nextPoint = path.get(i);
            double distance = previousPoint.toLa4j().subtract(nextPoint.toLa4j()).fold(Vectors.mkEuclideanNormAccumulator());
            double travelTimeInMillis = distance / unitsPerSecond / 1000;
            travelTimeSoFar += travelTimeInMillis;
            keyFrames.put(Math.round(travelTimeSoFar), nextPoint);
        }
        this.immerseScenarioSettingsBuilder.sourceLocation(KeyFramesDynamicLocation.keyFrames(keyFrames, loop));
        return this;
    }

    @Override
    // TODO: unit test
    public ScenarioBuilder sourceCircling(Vector3D center, double startAngle, double radius, double unitsPerSecond, boolean clockwise) {
        double circumference = 2 * Math.PI * radius;
        double milliesForCircumference = circumference / unitsPerSecond / 1000;
        double millisPerDegreeAngle = milliesForCircumference / 360;
        this.immerseScenarioSettingsBuilder
                .sourceLocation(HorizontalCircleDynamicLocation.horizontalCircle(center, startAngle, radius, clockwise, millisPerDegreeAngle));
        return this;
    }

    @Override
    public ScenarioBuilder listenerAtCenter() {
        this.immerseScenarioSettingsBuilder.listenerLocation(this.calculateCenterOfRoom());
        return this;
    }

    @Override
    public ScenarioBuilder fieldOfHearingVolume() {
        this.immerseScenarioSettingsBuilder.volumeRatiosAlgorithm(FieldOfHearingVolumeRatiosAlgorithm.fieldOfHearing());
        return this;
    }

    @Override
    public ScenarioBuilder fieldOfHearingVolume(double angle) {
        this.immerseScenarioSettingsBuilder.volumeRatiosAlgorithm(FieldOfHearingVolumeRatiosAlgorithm.fieldOfHearing(angle));
        return this;
    }

    @Override
    public ScenarioBuilder fixedVolumesRelative(Map<Integer, Double> relativeSpeakerVolumes) {
        this.immerseScenarioSettingsBuilder.volumeRatiosAlgorithm(FixedVolumeRatiosAlgorithm.fixed(new SpeakerVolumeRatios(relativeSpeakerVolumes)));
        return this;
    }

    @Override
    public ScenarioBuilder fixedVolumesAbsolute(Map<Integer, Double> absoluteSpeakerVolumes) {
        this.immerseScenarioSettingsBuilder.volumeRatiosAlgorithm(FixedVolumeRatiosAlgorithm.fixed(new SpeakerVolumeRatios(absoluteSpeakerVolumes)));
        double totalVolume = StreamEx.of(absoluteSpeakerVolumes.values()).mapToDouble(d -> d).sum();
        // By setting the normalize algorithm to a max sum that exactly matches the total volume amount, it will not alter the absolute values given.
        this.immerseScenarioSettingsBuilder.normalizeAlgorithm(MaxSumNormalizeAlgorithm.maxSum(totalVolume));
        return this;
    }

    @Override
    public ScenarioBuilder normalizeVolume() {
        this.immerseScenarioSettingsBuilder.normalizeAlgorithm(FractionalNormalizeAlgorithm.fractional());
        return this;
    }

    @Override
    public ScenarioBuilder volumeAsOneSpeaker() {
        this.immerseScenarioSettingsBuilder.normalizeAlgorithm(MaxSumNormalizeAlgorithm.maxSum(1));
        return this;
    }

    @Override
    public ScenarioBuilder maxSumVolume(double maxSum) {
        this.immerseScenarioSettingsBuilder.normalizeAlgorithm(MaxSumNormalizeAlgorithm.maxSum(maxSum));
        return this;
    }

    @Override
    public ScenarioBuilder playOnce() {
        this.immerseScenarioSettingsBuilder.playback(LoopPlayback.once());
        return this;
    }

    @Override
    public ScenarioBuilder playRepeat(int times) {
        this.immerseScenarioSettingsBuilder.playback(LoopPlayback.times(times));
        return this;
    }

    @Override
    public ScenarioBuilder playRepeatForever() {
        this.immerseScenarioSettingsBuilder.playback(ForeverPlayback.forever());
        return this;
    }

    @Override
    public ScenarioBuilder playForDuration(Duration duration) {
        this.immerseScenarioSettingsBuilder.playback(TimerPlayback.timer(duration.toMillis()));
        return this;
    }

    @Override
    public Scenario build() {
        return this.immerseScenarioBuilder.settings(this.immerseScenarioSettingsBuilder.build()).build();
    }

    private Factory<DynamicLocation> calculateCenterOfRoom() {
        return FixedDynamicLocation.fixed(
                this.calculateCenterOfAxis(Vector3D::getX),
                this.calculateCenterOfAxis(Vector3D::getY),
                this.calculateCenterOfAxis(Vector3D::getZ));
    }

    private double calculateCenterOfAxis(ToDoubleFunction<Vector3D> axisValueFunction) {
        Collection<Speaker> speakers = this.immerseSettings.getRoom().getSpeakers().values();
        return StreamEx.of(speakers)
                .map(Speaker::getPosition)
                .mapToDouble(axisValueFunction)
                .sum() / speakers.size();
    }

    private Map<Integer, Double> volumeAtSpeakers(Collection<Integer> speakerIds, double volume) {
        return StreamEx.of(this.immerseSettings.getRoom().getSpeakers().keySet())
                .toMap(speakerId -> speakerId, speakerId -> speakerIds.contains(speakerId) ? volume : 0.0);
    }

}
