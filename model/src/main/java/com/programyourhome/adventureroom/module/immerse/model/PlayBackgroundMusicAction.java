package com.programyourhome.adventureroom.module.immerse.model;

import java.util.Optional;

import com.programyourhome.adventureroom.model.script.action.Action;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.Volume;

//TODO: try to merge with play audio action! -> and have methods for parsing in abstract base class
public class PlayBackgroundMusicAction implements Action {

    public String filename;
    public Optional<Volume> volume = Optional.empty();

}
