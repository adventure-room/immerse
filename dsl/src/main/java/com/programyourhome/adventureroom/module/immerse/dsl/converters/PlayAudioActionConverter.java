package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.programyourhome.adventureroom.dsl.regex.MatchResult;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction;
import com.programyourhome.adventureroom.module.immerse.model.SpeakerExternalResource;
import com.programyourhome.immerse.domain.location.Vector3D;
import com.programyourhome.immerse.domain.speakers.Speaker;

import one.util.streamex.StreamEx;

public class PlayAudioActionConverter extends AbstractImmerseActionConverter<PlayAudioAction> {

    @Override
    public Map<String, String> getRegexMap() {
        String basePlay = "play " + FILENAME;
        return this.createRegexes(
                DEFAULT, basePlay + this.optionalAtVolume(),
                SINGLE, basePlay + " at speaker " + SPEAKER_ID + this.optionalAtVolume(),
                MULTIPLE, basePlay + " at speakers " + SPEAKER_IDS + this.optionalAtVolume(),
                ALL, basePlay + " at all speakers" + this.optionalAtVolume(),
                "location", basePlay + " at location " + LOCATION);
    }

    @Override
    public PlayAudioAction convert(MatchResult matchResult, Adventure adventure) {
        PlayAudioAction action = new PlayAudioAction();
        action.filename = matchResult.getValue(FILENAME);
        Optional<Collection<Integer>> optionalSpeakerIds = Optional.empty();
        if (matchResult.is(SINGLE)) {
            optionalSpeakerIds = Optional.of(Arrays.asList(Integer.valueOf(matchResult.getValue(SPEAKER_ID))));
        } else if (matchResult.is(MULTIPLE)) {
            optionalSpeakerIds = Optional.of(StreamEx.of(matchResult.getValue(SPEAKER_IDS).split(",")).map(Integer::valueOf).toList());
        } else if (matchResult.is(ALL)) {
            optionalSpeakerIds = Optional.of(StreamEx.of(adventure.getExternalResources(SpeakerExternalResource.class)).map(Speaker::getId).toList());
        }
        action.speakerIds = optionalSpeakerIds;
        action.sourceLocation = matchResult.getOptionalValue(LOCATION)
                .map(location -> location.split(","))
                .map(coordinates -> new Vector3D(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]), Double.valueOf(coordinates[2])));
        action.volume = matchResult.getOptionalValue(VOLUME).map(Double::valueOf);
        return action;
    }

}
