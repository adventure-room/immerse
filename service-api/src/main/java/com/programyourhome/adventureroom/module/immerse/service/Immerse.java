package com.programyourhome.adventureroom.module.immerse.service;

import java.util.Collection;
import java.util.UUID;

import com.programyourhome.immerse.domain.speakers.Speaker;

public interface Immerse {

    public void connect(String host, int port);

    public UUID playAtSpeakers(String url, Collection<Speaker> speakers, boolean loop, boolean waitFor);

    public void waitForPlayback(UUID playbackID);

    public void stopPlayback(UUID playbackId);

    public void quit();

}
