package com.programyourhome.adventureroom.module.immerse.model;

import java.util.Optional;

import com.programyourhome.adventureroom.model.script.action.Action;

//TODO: try to merge with play audio action?
public class PlayBackgroundMusicAction implements Action {

    public String filename;
    public Optional<Double> volume;

}
