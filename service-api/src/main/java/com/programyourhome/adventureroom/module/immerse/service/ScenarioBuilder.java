package com.programyourhome.adventureroom.module.immerse.service;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;
import com.programyourhome.immerse.domain.location.Vector3D;

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

    public ScenarioBuilder sourceAtSpeaker(int speakerId);

    public ScenarioBuilder sourceAtSpeakers(Collection<Integer> speakerIds);

    /**
     * Set the source position to be all speakers together.
     * This means no direction is taken into account.
     * NB: This is the default setting.
     */
    public ScenarioBuilder sourceAtAllSpeakers();

    public ScenarioBuilder sourceAtLocation(Vector3D location);

    // TODO: Create path builder with keyframes and possible other options like accelerate etc
    public ScenarioBuilder sourceAtPath(Map<Integer, Vector3D> path, boolean loop);

    // TODO: Create circling builder
    public ScenarioBuilder sourceCircling(Vector3D center, double radius, double unitsPerSecond, boolean clockwise);

    /**
     * Set the listener position to be static in the center of the room.
     * NB: This is the default setting.
     */
    public ScenarioBuilder listenerAtCenter();

    /**
     * Define the volume of the audio by the direction of the source
     * relative to the listener.
     * NB: This is the default setting.
     */
    public ScenarioBuilder fieldOfHearingVolume();

    public ScenarioBuilder fieldOfHearingVolume(double angle);

    public ScenarioBuilder fixedVolumesRelative(Map<Integer, Double> relativeSpeakerVolumes);

    public ScenarioBuilder fixedVolumesAbsolute(Map<Integer, Double> absoluteSpeakerVolumes);

    // Default
    public ScenarioBuilder normalizeVolume();

    /**
     * Set the volumes such that the total volume is equal to that of the one speaker.
     * Useful in combination with the field of hearing volume, so the total volume
     * will always be roughly equivalent, independent of the amount of speakers in 'range'.
     */
    public ScenarioBuilder volumeAsOneSpeaker();

    public ScenarioBuilder maxSumVolume(double maxSum);

    // default
    public ScenarioBuilder playOnce();

    public ScenarioBuilder playRepeat(int times);

    public ScenarioBuilder playRepeatForever();

    public ScenarioBuilder playForDuration(Duration duration);

    public Scenario build();
}