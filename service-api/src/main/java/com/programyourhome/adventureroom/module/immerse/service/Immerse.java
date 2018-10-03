package com.programyourhome.adventureroom.module.immerse.service;

import java.util.UUID;

import com.programyourhome.immerse.domain.ImmerseSettings;
import com.programyourhome.immerse.domain.Scenario;

public interface Immerse {

    public void connect(ImmerseSettings settings, String host, int port);

    public ImmerseSettings getSettings();

    public ScenarioBuilder scenarioBuilder();

    public UUID playScenario(Scenario scenario);

    public void waitForPlayback(UUID playbackID);

    public void fadeOutPlayback(UUID playbackId, int millis);

    public void stopPlayback(UUID playbackId);

    public void quit();

}
