package com.programyourhome.adventureroom.module.immerse.service;

import static com.programyourhome.immerse.toolbox.audio.playback.ForeverPlayback.forever;
import static com.programyourhome.immerse.toolbox.audio.playback.LoopPlayback.once;
import static com.programyourhome.immerse.toolbox.audio.resource.UrlAudioResource.urlWithFormat;
import static com.programyourhome.immerse.toolbox.audio.resource.UrlAudioResource.urlWithType;
import static com.programyourhome.immerse.toolbox.location.dynamic.FixedDynamicLocation.fixed;
import static com.programyourhome.immerse.toolbox.speakers.algorithms.normalize.FractionalNormalizeAlgorithm.fractional;
import static com.programyourhome.immerse.toolbox.speakers.algorithms.volumeratios.FixedVolumeRatiosAlgorithm.fixed;
import static com.programyourhome.immerse.toolbox.util.TestData.room;
import static com.programyourhome.immerse.toolbox.util.TestData.scenario;
import static com.programyourhome.immerse.toolbox.util.TestData.settings;
import static com.programyourhome.immerse.toolbox.util.TestData.soundCard;
import static com.programyourhome.immerse.toolbox.util.TestData.speaker;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.programyourhome.immerse.domain.Factory;
import com.programyourhome.immerse.domain.Room;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.playback.Playback;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;
import com.programyourhome.immerse.domain.audio.resource.AudioResource;
import com.programyourhome.immerse.domain.audio.soundcard.SoundCard;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;
import com.programyourhome.immerse.domain.format.SampleRate;
import com.programyourhome.immerse.domain.format.SampleSize;
import com.programyourhome.immerse.domain.speakers.Speaker;
import com.programyourhome.immerse.domain.speakers.SpeakerVolumeRatios;
import com.programyourhome.immerse.network.client.ImmerseClient;

import one.util.streamex.StreamEx;

public class ImmerseImpl implements Immerse {

    private ImmerseClient client;

    @Override
    public void connect(String host, int port) {
        this.client = new ImmerseClient(host, port);
        if (this.client.hasMixer().getResult()) {
            this.client.stopMixer();
        }
        this.client.createMixer(this.getRoom(), this.getSoundCards(), this.getAudioFormat());
        this.client.startMixer();
        System.out.println("Immerse client connection successful!");
    }

    // TODO: refactor: at least remove duplication, maybe change api to more matching methods
    // Maybe have some kind of PlaybackConfig thingy with a builder that covers all different setups somehow...
    @Override
    public UUID playAtSpeakers(String url, AudioFileType type, Collection<Speaker> speakers, boolean loop, boolean waitFor) {
        UUID playbackId = this.client.playScenario(this.createScenario(urlWithType(url, type, false), speakers, loop)).getResult();
        if (waitFor) {
            this.client.waitForPlayback(playbackId);
        }
        return playbackId;
    }

    @Override
    public UUID playAtSpeakers(String url, ImmerseAudioFormat format, Collection<Speaker> speakers, boolean loop, boolean waitFor) {
        UUID playbackId = this.client.playScenario(this.createScenario(urlWithFormat(url, format, false), speakers, loop)).getResult();
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

    private Scenario createScenario(Factory<AudioResource> factoryAudioResource, Collection<Speaker> speakers, boolean loop) {
        Factory<Playback> playback = loop ? forever() : once();
        // TODO: convenience method for all speakers
        Collection<Integer> speakerIds = StreamEx.of(speakers).map(Speaker::getId).toList();
        SpeakerVolumeRatios fixedSpeakerVolumeRatios = new SpeakerVolumeRatios(
                StreamEx.of(this.getRoom().getSpeakers().values())
                        .map(Speaker::getId)
                        .toMap(id -> id, id -> speakerIds.contains(id) ? 1.0 : 0.0));
        Scenario scenario = scenario(this.getRoom(),
                settings(factoryAudioResource, fixed(100, 60, 80), fixed(100, 60, 80),
                        fixed(fixedSpeakerVolumeRatios), fractional(), playback));
        return scenario;
    }

    // TODO: should get this info from the adventure config
    Speaker speaker1 = speaker(1, 0, 366, 250);
    Speaker speaker2 = speaker(2, 122, 366, 250);
    Speaker speaker3 = speaker(3, 244, 366, 250);
    Speaker speaker4 = speaker(4, 366, 366, 250);
    Speaker speaker5 = speaker(5, 366, 244, 250);
    Speaker speaker6 = speaker(6, 366, 122, 250);
    Speaker speaker7 = speaker(7, 366, 0, 250);
    Speaker speaker8 = speaker(8, 244, 0, 250);
    Speaker speaker9 = speaker(9, 122, 0, 250);
    Speaker speaker10 = speaker(10, 0, 0, 250);
    Speaker speaker11 = speaker(11, 0, 122, 250);
    Speaker speaker12 = speaker(12, 0, 244, 250);

    private Room getRoom() {
        return room(UUID.fromString("3e8b23c8-f83b-497f-b0ce-fcfcc6a39ced"), this.speaker1, this.speaker2, this.speaker3,
                this.speaker4, this.speaker5, this.speaker6, this.speaker7, this.speaker8, this.speaker9, this.speaker10, this.speaker11, this.speaker12);
    }

    private List<SoundCard> getSoundCards() {
        SoundCard soundCard1 = soundCard(1, "platform-1c1b000.ehci1-controller-usb-0:1.2:1.0", this.speaker9, this.speaker6);
        SoundCard soundCard2 = soundCard(2, "platform-1c1b000.ehci1-controller-usb-0:1.3:1.0", this.speaker10, this.speaker11);
        SoundCard soundCard3 = soundCard(3, "platform-1c1b000.ehci1-controller-usb-0:1.4:1.0", this.speaker7, this.speaker4);
        SoundCard soundCard4 = soundCard(4, "platform-1c1b000.ehci1-controller-usb-0:1.1.2:1.0", this.speaker1, this.speaker12);
        SoundCard soundCard5 = soundCard(5, "platform-1c1b000.ehci1-controller-usb-0:1.1.3:1.0", this.speaker8, this.speaker5);
        SoundCard soundCard6 = soundCard(6, "platform-1c1b000.ehci1-controller-usb-0:1.1.4:1.0", this.speaker3, this.speaker2);

        return Arrays.asList(soundCard1, soundCard2, soundCard3, soundCard4, soundCard5, soundCard6);
    }

    private ImmerseAudioFormat getAudioFormat() {
        return ImmerseAudioFormat.builder()
                .sampleRate(SampleRate.RATE_44K)
                .sampleSize(SampleSize.TWO_BYTES)
                .buildForOutput();
    }

}
