package com.programyourhome.adventureroom.module.immerse.service;

import java.util.UUID;

import com.programyourhome.immerse.domain.ImmerseSettings;
import com.programyourhome.immerse.domain.Scenario;
import com.programyourhome.immerse.network.client.ImmerseClient;

public class ImmerseImpl implements Immerse {

    private ImmerseClient client;
    private ImmerseSettings settings;

    @Override
    public void connect(ImmerseSettings settings, String host, int port) {
        this.client = new ImmerseClient(host, port);
        if (this.client.hasMixer().getResult()) {
            this.client.stopMixer();
        }
        this.client.createMixer(settings);
        this.client.startMixer();
        this.settings = settings;
        System.out.println("Immerse client connection successful!");
    }

    @Override
    public ImmerseSettings getSettings() {
        return this.settings;
    }

    @Override
    public ScenarioBuilder scenarioBuilder() {
        return new ScenarioBuilderImpl(this.settings);
    }

    @Override
    public UUID playScenario(Scenario scenario) {
        return this.client.playScenario(scenario).getResult();
    }

    @Override
    public void waitForPlayback(UUID playbackId) {
        this.client.waitForPlayback(playbackId);
    }

    @Override
    public void fadeOutPlayback(UUID playbackId, int millis) {
        this.client.fadeOutPlayback(playbackId, millis);
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

}
