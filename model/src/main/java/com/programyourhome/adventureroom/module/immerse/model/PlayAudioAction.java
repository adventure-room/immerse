package com.programyourhome.adventureroom.module.immerse.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.programyourhome.adventureroom.model.either.Either3;
import com.programyourhome.adventureroom.model.either.EitherOrNone;
import com.programyourhome.adventureroom.model.either.Yes;
import com.programyourhome.adventureroom.model.script.action.Action;
import com.programyourhome.immerse.domain.location.Vector3D;

public class PlayAudioAction implements Action {

    // TODO: validation logic for which can go together
    public String filename;
    public Optional<Integer> volume = Optional.empty();
    public SoundSource soundSource;
    public Either3<Vector3D, Path, Circling> listenerLocation;
    public Optional<Normalize> normalize = Optional.empty();
    public Playback playback;

    public class Path {
        public List<Vector3D> waypoints;
        public double speed;
    }

    public class SoundSource extends EitherOrNone {
        public SoundSource(Optional<Collection<Integer>> speakerIds, Optional<Vector3D> staticLocation, Optional<Path> path, Optional<Circling> circling) {
            super(speakerIds, staticLocation, path, circling);
        }

        public Optional<Collection<Integer>> getSpeakerIds() {
            return this.getItem(1);
        }

        public Optional<Vector3D> getStaticLocation() {
            return this.getItem(2);
        }

        public Optional<Path> getPath() {
            return this.getItem(3);
        }

        public Optional<Circling> getCircling() {
            return this.getItem(4);
        }
    }

    public class Circling {
        public Vector3D center;
        public double radius;
        public Optional<Double> startAngle = Optional.empty();
        public Optional<Boolean> clockwise = Optional.empty();
        public double speed;
    }

    public class Playback extends EitherOrNone {
        public Playback(Optional<Yes> once, Optional<Integer> repeat, Optional<Yes> forever) {
            super(once, repeat, forever);
        }

        public Optional<Yes> getOnce() {
            return this.getItem(1);
        }

        public Optional<Integer> getRepeat() {
            return this.getItem(2);
        }

        public Optional<Yes> getForever() {
            return this.getItem(3);
        }

    }

    public enum Normalize {
        AS_ONE_SPEAKER, AS_ALL_SPEAKERS;
    }

}
