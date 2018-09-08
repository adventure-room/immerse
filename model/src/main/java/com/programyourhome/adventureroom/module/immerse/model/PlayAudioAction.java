package com.programyourhome.adventureroom.module.immerse.model;

import java.util.Collection;
import java.util.Optional;

import com.programyourhome.adventureroom.model.script.action.Action;
import com.programyourhome.immerse.domain.location.Vector3D;

public class PlayAudioAction implements Action {

    // TODO: validation logic for which can go together
    public String filename;
    public Optional<Collection<Integer>> speakerIds = Optional.empty();
    public Optional<Vector3D> sourceLocation = Optional.empty();
    public Optional<Double> volume = Optional.empty();

}
