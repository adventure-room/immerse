package com.programyourhome.adventureroom.immerse.service;

import static com.programyourhome.immerse.toolbox.audio.playback.ForeverPlayback.forever;
import static com.programyourhome.immerse.toolbox.audio.resource.UrlAudioResource.url;
import static com.programyourhome.immerse.toolbox.location.dynamic.FixedDynamicLocation.fixed;
import static com.programyourhome.immerse.toolbox.location.dynamic.KeyFramesDynamicLocation.keyFrames;
import static com.programyourhome.immerse.toolbox.speakers.algorithms.normalize.MaxSumNormalizeAlgorithm.maxSum;
import static com.programyourhome.immerse.toolbox.speakers.algorithms.volumeratios.FixedVolumeRatiosAlgorithm.fixed;
import static com.programyourhome.immerse.toolbox.util.TestData.room;
import static com.programyourhome.immerse.toolbox.util.TestData.scenario;
import static com.programyourhome.immerse.toolbox.util.TestData.settings;
import static com.programyourhome.immerse.toolbox.util.TestData.soundCard;
import static com.programyourhome.immerse.toolbox.util.TestData.speaker;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import com.programyourhome.immerse.audiostreaming.format.ImmerseAudioFormat;
import com.programyourhome.immerse.audiostreaming.format.SampleRate;
import com.programyourhome.immerse.audiostreaming.format.SampleSize;
import com.programyourhome.immerse.domain.Room;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.domain.audio.soundcard.SoundCard;
import com.programyourhome.immerse.domain.location.Vector3D;
import com.programyourhome.immerse.domain.speakers.Speaker;
import com.programyourhome.immerse.domain.speakers.SpeakerVolumeRatios;
import com.programyourhome.immerse.network.client.ImmerseClient;
import com.programyourhome.immerse.network.server.action.ActionResult;

public class ImmerseImpl implements Immerse {

    private ImmerseClient client;

    @Override
    public void configure(String host, int port) {
        this.client = new ImmerseClient(host, port);
        this.client.createMixer(this.getRoom(), this.getSoundCards(), this.getAudioFormat());
        this.client.startMixer();
        System.out.println("Immerse client connection successful!");
    }

    @Override
    public void playBackgroundMusic(String audioId) {
        ActionResult<UUID> result = this.client.playScenario(this.createScenario(audioId));
        System.out.println(result.getResult());
    }

    private Scenario createScenario(String audioId) {
        // TODO: convenience class around key frames?
        // TODO: key frames options loop or once
        SortedMap<Long, Vector3D> keyFrames = new TreeMap<>();
        keyFrames.put(0L, new Vector3D(0, 0, 80));
        keyFrames.put(3_000L, new Vector3D(0, 100, 80));
        keyFrames.put(6_000L, new Vector3D(200, 120, 80));
        keyFrames.put(9_000L, new Vector3D(200, 50, 80));
        keyFrames.put(12_000L, new Vector3D(0, 0, 80));

        SpeakerVolumeRatios fixedSpeakerVolumeRatios = new SpeakerVolumeRatios(
                this.getRoom().getSpeakers().values().stream().collect(Collectors.toMap(Speaker::getId, speaker -> 1.0)));
        Scenario scenario = scenario(this.getRoom(),
                settings(url("http://10.42.0.1:19161/audio/" + audioId), keyFrames(keyFrames), fixed(100, 60, 80),
                        fixed(fixedSpeakerVolumeRatios), maxSum(1), forever()));
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
        SoundCard soundCard1 = soundCard(1, "platform-1c1b000.ehci1-controller-usb-0:1.2:1.0", this.getSpeaker2(), this.getSpeaker1());
        SoundCard soundCard2 = soundCard(2, "platform-1c1b000.ehci1-controller-usb-0:1.4:1.0", this.getSpeaker3(), this.getSpeaker4());
        return Arrays.asList(soundCard1, soundCard2);
    }

    private ImmerseAudioFormat getAudioFormat() {
        return ImmerseAudioFormat.builder()
                .sampleRate(SampleRate.RATE_44K)
                .sampleSize(SampleSize.TWO_BYTES)
                .buildForOutput();
    }

}
