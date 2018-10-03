package com.programyourhome.adventureroom.module.immerse.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.programyourhome.adventureroom.model.either.Either;
import com.programyourhome.adventureroom.model.either.Yes;
import com.programyourhome.adventureroom.model.script.action.Action;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;
import com.programyourhome.immerse.domain.location.Vector3D;

public class PlayAudioAction implements Action {

    public Resource resource;
    public Optional<Volume> volume = Optional.empty();
    public Optional<SoundSource> soundSource = Optional.empty();
    public Optional<DynamicLocation> listenerLocation = Optional.empty();
    public Optional<Normalize> normalize = Optional.empty();
    public Optional<Playback> playback = Optional.empty();
    public Optional<String> saveAsVariable = Optional.empty();

    public static class Resource extends Either {
        public static Resource file(String filename) {
            return new Resource(Optional.of(filename), Optional.empty());
        }

        public static Resource url(UrlResource urlResource) {
            return new Resource(Optional.empty(), Optional.of(urlResource));
        }

        public Resource(Optional<String> filename, Optional<UrlResource> url) {
            super(filename, url);
        }

        public Optional<String> getFilename() {
            return this.getItem(1);
        }

        public Optional<UrlResource> getUrl() {
            return this.getItem(2);
        }

        @Override
        public String toString() {
            return this.getFilename().orElse("") + this.getUrl().map(urlResource -> urlResource.urlString).orElse("");
        }
    }

    public static class Volume {
        public int volumePercentage;
        public Optional<Integer> fadeInMillis = Optional.empty();
    }

    public static class UrlResource {
        public String urlString;
        public Optional<ImmerseAudioFormat> audioFormat = Optional.empty();
    }

    public static class SoundSource extends Either {
        public static SoundSource speakerIds(Collection<Integer> speakerIds) {
            return new SoundSource(Optional.of(speakerIds), Optional.empty());
        }

        public static SoundSource dynamicLocation(DynamicLocation dynamicLocation) {
            return new SoundSource(Optional.empty(), Optional.of(dynamicLocation));
        }

        public SoundSource(Optional<Collection<Integer>> speakerIds, Optional<DynamicLocation> dynamicLocation) {
            super(speakerIds, dynamicLocation);
        }

        public Optional<Collection<Integer>> getSpeakerIds() {
            return this.getItem(1);
        }

        public Optional<DynamicLocation> getDynamicLocation() {
            return this.getItem(2);
        }
    }

    public static class DynamicLocation extends Either {
        public static DynamicLocation staticLocation(Vector3D staticLocation) {
            return new DynamicLocation(Optional.of(staticLocation), Optional.empty(), Optional.empty());
        }

        public static DynamicLocation path(Path path) {
            return new DynamicLocation(Optional.empty(), Optional.of(path), Optional.empty());
        }

        public static DynamicLocation circling(Circling circling) {
            return new DynamicLocation(Optional.empty(), Optional.empty(), Optional.of(circling));
        }

        public DynamicLocation(Optional<Vector3D> staticLocation, Optional<Path> path, Optional<Circling> circling) {
            super(staticLocation, path, circling);
        }

        public Optional<Vector3D> getStaticLocation() {
            return this.getItem(1);
        }

        public Optional<Path> getPath() {
            return this.getItem(2);
        }

        public Optional<Circling> getCircling() {
            return this.getItem(3);
        }
    }

    public static class Path {
        public List<Vector3D> waypoints;
        public double speed;
    }

    public static class Circling {
        public Vector3D center;
        public double radius;
        public Optional<Double> startAngle = Optional.empty();
        public Optional<Boolean> clockwise = Optional.empty();
        public double speed;
    }

    public static class Normalize extends Either {
        public static Normalize asOneSpeaker() {
            return new Normalize(Optional.of(Yes.Y), Optional.empty());
        }

        public static Normalize asAllSpeakers() {
            return new Normalize(Optional.empty(), Optional.of(Yes.Y));
        }

        public Normalize(Optional<Yes> asOneSpeaker, Optional<Yes> asAllSpeakers) {
            super(asOneSpeaker, asAllSpeakers);
        }

        public Optional<Yes> getAsOneSpeaker() {
            return this.getItem(1);
        }

        public Optional<Yes> getAsAllSpeakers() {
            return this.getItem(2);
        }
    }

    public static class Playback extends Either {
        public static Playback once() {
            return new Playback(Optional.of(Yes.Y), Optional.empty(), Optional.empty(), Optional.empty());
        }

        public static Playback repeat(int times) {
            return new Playback(Optional.empty(), Optional.of(times), Optional.empty(), Optional.empty());
        }

        public static Playback forever() {
            return new Playback(Optional.empty(), Optional.empty(), Optional.of(Yes.Y), Optional.empty());
        }

        public static Playback seconds(int seconds) {
            return new Playback(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(seconds));
        }

        public Playback(Optional<Yes> once, Optional<Integer> repeat, Optional<Yes> forever, Optional<Integer> seconds) {
            super(once, repeat, forever, seconds);
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

        public Optional<Integer> getSeconds() {
            return this.getItem(4);
        }

    }

}
