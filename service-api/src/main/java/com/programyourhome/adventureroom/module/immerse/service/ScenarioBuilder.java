package com.programyourhome.adventureroom.module.immerse.service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.programyourhome.immerse.domain.Factory;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;
import com.programyourhome.immerse.domain.location.Vector3D;
import com.programyourhome.immerse.domain.location.dynamic.DynamicLocation;

/**
 * NB: When the source is defined to be one or more speakers, the volume calculation setting is ignored.
 */
public interface ScenarioBuilder {

    /**
     * Give the scenario a name (optional).
     */
    public ScenarioBuilder name(String name);

    /**
     * Give the scenario a description (optional).
     */
    public ScenarioBuilder description(String description);

    /**
     * Set the audio resource to be a file with the given absolute path.
     * NB: This is the path on the Immerse server!
     */
    public ScenarioBuilder file(String path);

    /**
     * Set the audio resource to be the url with the given audio format.
     * NB: The url must be relative to the Immerse server, so beware of localhost.
     */
    public ScenarioBuilder urlWithFormat(String url, ImmerseAudioFormat format);

    public ScenarioBuilder urlWithType(String url, AudioFileType type);

    public ScenarioBuilder volume(double volume);

    // Default
    public ScenarioBuilder fullVolume();

    public ScenarioBuilder muteVolume();

    public ScenarioBuilder linearVolume(double from, double to, long inMillis);

    public ScenarioBuilder linearVolumeWithDelay(double from, double to, long inMillis, long delayMillis);

    public ScenarioBuilder sourceAtSpeaker(int speakerId);

    public ScenarioBuilder sourceAtSpeakers(Collection<Integer> speakerIds);

    /**
     * Set the source position to be all speakers together.
     * This means no direction is taken into account.
     * NB: This is the default setting.
     */
    public ScenarioBuilder sourceAtAllSpeakers();

    public Factory<DynamicLocation> atLocation(Vector3D location);

    // TODO: Create path builder with keyframes and possible other options like accelerate etc
    // TODO: idea: instead of just units per second, have something like a Speed that can be set
    // with static, dynamic speeds and human readable options like walking, running, etc. (where 1 unit is set to 1 match 1 cm or m or ?)
    public Factory<DynamicLocation> atPath(List<Vector3D> path, double unitsPerSecond, boolean loop);

    // TODO: Create circling builder
    public Factory<DynamicLocation> circling(Vector3D center, double startAngle, double radius, double unitsPerSecond, boolean clockwise);

    public Factory<DynamicLocation> atCenter();

    /**
     * Define the volume of the audio by the direction of the source
     * relative to the listener.
     * NB: This is the default setting.
     */
    public ScenarioBuilder fieldOfHearingVolume(Factory<DynamicLocation> sourceLocation, Factory<DynamicLocation> listenerLocation);

    public ScenarioBuilder fieldOfHearingVolume(Factory<DynamicLocation> sourceLocation, Factory<DynamicLocation> listenerLocation, double angle);

    public ScenarioBuilder fixedVolumesRelative(Map<Integer, Double> relativeSpeakerVolumes);

    public ScenarioBuilder normalizeVolume();

    /**
     * Set the volumes such that the total volume is equal to that of the one speaker.
     * Useful in combination with the field of hearing volume, so the total volume
     * will always be roughly equivalent, independent of the amount of speakers in 'range'.
     */
    // Default
    public ScenarioBuilder volumeAsOneSpeaker();

    public ScenarioBuilder maxSumVolume(double maxSum);

    // default
    public ScenarioBuilder playOnce();

    public ScenarioBuilder playRepeat(int times);

    public ScenarioBuilder playRepeatForever();

    public ScenarioBuilder playForDuration(Duration duration);

    public Scenario build();
}
