package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import java.util.Optional;

import com.programyourhome.adventureroom.dsl.antlr.AbstractReflectiveParseTreeAntlrActionConverter;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.BackgroundResourceSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.PlayBackgroundMusicActionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.VolumeSectionContext;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction.Volume;
import com.programyourhome.adventureroom.module.immerse.model.PlayBackgroundMusicAction;

//TODO: merge with play audio action!!
public class PlayBackgroundMusicActionConverter
        extends AbstractReflectiveParseTreeAntlrActionConverter<PlayBackgroundMusicActionContext, PlayBackgroundMusicAction> {

    // TODO: make parameter for this
    public static final double DEFAULT_BACKGROUND_MUSIC_VOLUME = 0.3;

    public void parseBackgroundResourceSection(BackgroundResourceSectionContext context, PlayBackgroundMusicAction action) {
        action.filename = this.toString(context.filename);
    }

    public void parseVolumeSection(VolumeSectionContext context, PlayBackgroundMusicAction action) {
        Volume volume = new Volume();
        volume.volumePercentage = this.toInt(context.volume);
        Optional.ofNullable(context.fadeIn).ifPresent(fadeIn -> volume.fadeInMillis = Optional.of((int) (this.toDouble(fadeIn) * 1000)));
        action.volume = Optional.of(volume);
    }

}
