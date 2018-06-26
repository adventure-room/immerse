package com.programyourhome.adventureroom.module.immerse.service;

import static com.programyourhome.immerse.toolbox.audio.playback.ForeverPlayback.forever;
import static com.programyourhome.immerse.toolbox.audio.playback.LoopPlayback.once;
import static com.programyourhome.immerse.toolbox.audio.resource.UrlAudioResource.url;
import static com.programyourhome.immerse.toolbox.location.dynamic.FixedDynamicLocation.fixed;
import static com.programyourhome.immerse.toolbox.speakers.algorithms.normalize.MaxSumNormalizeAlgorithm.maxSum;
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

import com.programyourhome.adventureroom.module.immerse.service.Immerse;
import com.programyourhome.immerse.audiostreaming.format.ImmerseAudioFormat;
import com.programyourhome.immerse.audiostreaming.format.SampleRate;
import com.programyourhome.immerse.audiostreaming.format.SampleSize;
import com.programyourhome.immerse.domain.Factory;
import com.programyourhome.immerse.domain.Room;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.playback.Playback;
import com.programyourhome.immerse.domain.audio.soundcard.SoundCard;
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

    @Override
    public UUID playAtSpeakers(String url, Collection<Speaker> speakers, boolean loop, boolean waitFor) {
        UUID playbackId = this.client.playScenario(this.createScenario(url, speakers, loop)).getResult();
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
        this.client.stopMixer();
    }

    private Scenario createScenario(String url, Collection<Speaker> speakers, boolean loop) {
        Factory<Playback> playback = loop ? forever() : once();
        // TODO: convenience method for all speakers
        Collection<Integer> speakerIds = StreamEx.of(speakers).map(Speaker::getId).toList();
        SpeakerVolumeRatios fixedSpeakerVolumeRatios = new SpeakerVolumeRatios(
                StreamEx.of(this.getRoom().getSpeakers().values())
                        .map(Speaker::getId)
                        .toMap(id -> id, id -> speakerIds.contains(id) ? 1.0 : 0.0));
        Scenario scenario = scenario(this.getRoom(),
                settings(url(url), fixed(100, 60, 80), fixed(100, 60, 80),
                        fixed(fixedSpeakerVolumeRatios), maxSum(1), playback));
        return scenario;
    }

    private Speaker getSpeaker1() {
        return speaker(1, 0, 0, 80);
    }

    private Speaker getSpeaker2() {
        return speaker(2, 0, 100, 80);
    }

    private Speaker getSpeaker3() {
        return speaker(3, 200, 120, 80);
    }

    private Speaker getSpeaker4() {
        return speaker(4, 200, 50, 80);
    }

    private Room getRoom() {
        Speaker speaker1 = this.getSpeaker1();
        Speaker speaker2 = this.getSpeaker2();
        Speaker speaker3 = this.getSpeaker3();
        Speaker speaker4 = this.getSpeaker4();
        return room(UUID.fromString("3e8b23c8-f83b-497f-b0ce-fcfcc6a39ced"), speaker1, speaker2, speaker3, speaker4);
    }

    private List<SoundCard> getSoundCards() {
        SoundCard soundCard1 = soundCard(1, "pci-0000:00:1f.3", this.getSpeaker1(), this.getSpeaker2());

        // SoundCard soundCard1 = soundCard(1, "platform-1c1b000.ehci1-controller-usb-0:1.2:1.0", this.getSpeaker2(), this.getSpeaker1());
        // SoundCard soundCard2 = soundCard(2, "platform-1c1b000.ehci1-controller-usb-0:1.4:1.0", this.getSpeaker3(), this.getSpeaker4());
        return Arrays.asList(soundCard1);
    }

    private ImmerseAudioFormat getAudioFormat() {
        return ImmerseAudioFormat.builder()
                .sampleRate(SampleRate.RATE_44K)
                .sampleSize(SampleSize.TWO_BYTES)
                .buildForOutput();
    }

}
