package com.programyourhome.adventureroom.module.immerse.model;

import java.util.Optional;

import com.programyourhome.adventureroom.model.script.action.Action;

public class StopAudioAction implements Action {

    public String variableName;

    public Optional<Integer> fadeOutMillis;

}
