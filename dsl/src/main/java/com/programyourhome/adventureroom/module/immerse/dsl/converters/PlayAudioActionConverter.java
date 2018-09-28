package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.Token;

import com.programyourhome.adventureroom.dsl.antlr.AbstractReflectiveParseTreeAntlrActionConverter;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.AllSpeakersContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.CirclingLocationContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.FixedLocationContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.ListenerLocationSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.LocationSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.MultipleSpeakersContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.NormalizeSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.PathLocationContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.PlayAudioActionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.PlaybackSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.ResourceSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.SingleSpeakerContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.SourceLocationSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.SourceSpeakerSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.VolumeSectionContext;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.Circling;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.DynamicLocation;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.Normalize;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.Path;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.Playback;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.SoundSource;
import com.programyourhome.adventureroom.module.immerse.model.SpeakerExternalResource;
import com.programyourhome.immerse.domain.location.Vector3D;
import com.programyourhome.immerse.domain.speakers.Speaker;

import one.util.streamex.StreamEx;

public class PlayAudioActionConverter extends AbstractReflectiveParseTreeAntlrActionConverter<PlayAudioActionContext, PlayAudioAction> {

    public void parseResourceSection(ResourceSectionContext context, PlayAudioAction action) {
        action.filename = this.toString(context.filename);
    }

    public void parseVolumeSection(VolumeSectionContext context, PlayAudioAction action) {
        action.volume = Optional.of(this.toInt(context.volume));
    }

    public void parseSourceSpeakerSection(SourceSpeakerSectionContext context, PlayAudioAction action) {
        action.soundSource = Optional.of(SoundSource.speakerIds(this.getOne(
                this.parse(context.singleSpeaker(), this::parseSingleSpeaker),
                this.parse(context.multipleSpeakers(), this::parseMultipleSpeakers),
                this.parse(context.allSpeakers(), this::parseAllSpeakers))));
    }

    private Collection<Integer> parseSingleSpeaker(SingleSpeakerContext context) {
        return Arrays.asList(this.toInt(context.speakerId));
    }

    private Collection<Integer> parseMultipleSpeakers(MultipleSpeakersContext context) {
        return StreamEx.of(context.speakerIds.getText().split(",")).map(Integer::valueOf).toList();
    }

    private Collection<Integer> parseAllSpeakers(AllSpeakersContext context) {
        return StreamEx.of(this.getAdventure().getExternalResources(SpeakerExternalResource.class)).map(Speaker::getId).toList();
    }

    public void parseSourceLocationSection(SourceLocationSectionContext context, PlayAudioAction action) {
        action.soundSource = Optional.of(SoundSource.dynamicLocation(this.parseListenerSection(context.locationSection(), action)));
    }

    public void parseListenerLocationSection(ListenerLocationSectionContext context, PlayAudioAction action) {
        action.listenerLocation = Optional.of(this.parseListenerSection(context.locationSection(), action));
    }

    private DynamicLocation parseListenerSection(LocationSectionContext context, PlayAudioAction action) {
        return this.getOne(
                this.parse(context.fixedLocation(), this::parseFixedLocation),
                this.parse(context.pathLocation(), this::parsePathLocation),
                this.parse(context.circlingLocation(), this::parseCirclingLocation));
    }

    private DynamicLocation parseFixedLocation(FixedLocationContext context) {
        return DynamicLocation.staticLocation(this.parseVector3D(context.location));
    }

    private DynamicLocation parsePathLocation(PathLocationContext context) {
        Path path = new Path();
        String[] locationStrings = context.path.getText().split(";");
        path.waypoints = StreamEx.of(locationStrings).map(this::parseVector3D).toList();
        path.speed = this.toDouble(context.speed);
        return DynamicLocation.path(path);
    }

    private DynamicLocation parseCirclingLocation(CirclingLocationContext context) {
        Circling circling = new Circling();
        circling.clockwise = this.maybeOne(
                this.parse(context.clockwise, c -> true),
                this.parse(context.antiClockwise, ac -> false));
        circling.center = this.parseVector3D(context.center);
        circling.radius = this.toDouble(context.radius);
        circling.startAngle = this.toOptionalDouble(context.startAngle);
        circling.speed = this.toDouble(context.speed);
        return DynamicLocation.circling(circling);
    }

    private Vector3D parseVector3D(Token location) {
        return this.parseVector3D(location.getText());
    }

    private Vector3D parseVector3D(String location) {
        String strippedLocation = location.replaceAll("\\(", "").replaceAll("\\)", "");
        List<Double> coordinates = StreamEx.of(strippedLocation.split(",")).map(Double::parseDouble).toList();
        return new Vector3D(coordinates);
    }

    public void parsePlaybackSection(PlaybackSectionContext context, PlayAudioAction action) {
        action.playback = this.getOneAsOptional(
                this.parse(context.once, once -> Playback.once()),
                this.parse(context.repeat, times -> Playback.repeat(this.toInt(times))),
                this.parse(context.forever, forever -> Playback.forever()),
                this.parse(context.seconds, seconds -> Playback.seconds(this.toInt(seconds))));
    }

    public void parseNormalizeSection(NormalizeSectionContext context, PlayAudioAction action) {
        action.normalize = this.getOneAsOptional(
                this.parse(context.asOneSpeaker, one -> Normalize.asOneSpeaker()),
                this.parse(context.asAllSpeakers, all -> Normalize.asAllSpeakers()));
    }

}
