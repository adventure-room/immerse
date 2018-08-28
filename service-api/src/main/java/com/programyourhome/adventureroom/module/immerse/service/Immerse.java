package com.programyourhome.adventureroom.module.immerse.service;

import java.util.Collection;
import java.util.UUID;

import com.programyourhome.immerse.domain.ImmerseSettings;
import com.programyourhome.immerse.domain.audio.resource.AudioFileType;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;

public interface Immerse {

    public void connect(ImmerseSettings settings, String host, int port);

    public UUID playAtSpeakers(String url, ImmerseAudioFormat format, Collection<Integer> speakerIds, boolean loop, boolean waitFor);

    public UUID playAtSpeakers(String url, AudioFileType type, Collection<Integer> speakerIds, boolean loop, boolean waitFor);

    public void waitForPlayback(UUID playbackID);

    public void stopPlayback(UUID playbackId);

    public void quit();

}
