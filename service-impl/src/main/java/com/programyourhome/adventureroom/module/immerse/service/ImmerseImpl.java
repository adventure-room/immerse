package com.programyourhome.adventureroom.module.immerse.service;

import static com.programyourhome.immerse.toolbox.audio.playback.ForeverPlayback.forever;
import static com.programyourhome.immerse.toolbox.audio.playback.LoopPlayback.once;
import static com.programyourhome.immerse.toolbox.location.dynamic.FixedDynamicLocation.fixed;
import static com.programyourhome.immerse.toolbox.speakers.algorithms.normalize.FractionalNormalizeAlgorithm.fractional;
import static com.programyourhome.immerse.toolbox.speakers.algorithms.volumeratios.FixedVolumeRatiosAlgorithm.fixed;
import static com.programyourhome.immerse.toolbox.util.TestData.scenario;
import static com.programyourhome.immerse.toolbox.util.TestData.settings;

import java.util.Collection;
import java.util.UUID;

import com.programyourhome.immerse.domain.Factory;
import com.programyourhome.immerse.domain.ImmerseSettings;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.playback.Playback;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;
import com.programyourhome.immerse.domain.audio.resource.AudioResource;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;
import com.programyourhome.immerse.domain.speakers.Speaker;
import com.programyourhome.immerse.domain.speakers.SpeakerVolumeRatios;
import com.programyourhome.immerse.network.client.ImmerseClient;
import com.programyourhome.immerse.toolbox.audio.resource.UrlAudioResource;

import one.util.streamex.StreamEx;

public class ImmerseImpl implements Immerse {

    private ImmerseClient client;

    @Override
    public void connect(ImmerseSettings settings, String host, int port) {
        this.client = new ImmerseClient(host, port);
        if (this.client.hasMixer().getResult()) {
            this.client.stopMixer();
        }
        this.client.createMixer(settings);
        this.client.startMixer();
        System.out.println("Immerse client connection successful!");
    }

    // TODO: refactor: at least remove duplication, maybe change api to more matching methods
    // Maybe have some kind of PlaybackConfig thingy with a builder that covers all different setups somehow...
    @Override
    public UUID playAtSpeakers(String url, AudioFileType type, Collection<Speaker> speakers, boolean loop, boolean waitFor) {
        UUID playbackId = this.client.playScenario(this.createScenario(UrlAudioResource.urlWithType(url, type), speakers, loop)).getResult();
        if (waitFor) {
            this.client.waitForPlayback(playbackId);
        }
        return playbackId;
    }

    @Override
    public UUID playAtSpeakers(String url, ImmerseAudioFormat format, Collection<Speaker> speakers, boolean loop, boolean waitFor) {
        UUID playbackId = this.client.playScenario(this.createScenario(UrlAudioResource.urlWithFormat(url, format), speakers, loop)).getResult();
        if (waitFor) {
            this.client.waitForPlayback(playbackId);
        }
        return playbackId;
    }

    @Override
    public void waitForPlayback(UUID playbackID) {
        this.client.waitForPlayback(playbackID);
    }

    @Override
    public void stopPlayback(UUID playbackId) {
        this.client.stopPlayback(playbackId);
    }

    @Override
    public void quit() {
        if (this.client != null) {
            this.client.stopMixer();
        }
    }

    private Scenario createScenario(Factory<AudioResource> audioResourceFactory, Collection<Speaker> speakers, boolean loop) {
        ImmerseSettings settings = this.client.getSettings().getResult();
        Factory<Playback> playback = loop ? forever() : once();
        // TODO: convenience method for all speakers
        Collection<Integer> speakerIds = StreamEx.of(speakers).map(Speaker::getId).toList();
        SpeakerVolumeRatios fixedSpeakerVolumeRatios = new SpeakerVolumeRatios(
                StreamEx.of(settings.getRoom().getSpeakers().values())
                        .map(Speaker::getId)
                        .toMap(id -> id, id -> speakerIds.contains(id) ? 1.0 : 0.0));
        Scenario scenario = scenario(settings(audioResourceFactory, fixed(100, 60, 80), fixed(100, 60, 80),
                fixed(fixedSpeakerVolumeRatios), fractional(), playback));
        return scenario;
    }

}
