package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.antlr.AbstractReflectiveParseTreeAntlrActionConverter;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.BackgroundResourceSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.PlayBackgroundMusicActionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.VolumeSectionContext;
import com.programyourhome.adventureroom.module.immerse.model.PlayBackgroundMusicAction;

public class PlayBackgroundMusicActionConverter
        extends AbstractReflectiveParseTreeAntlrActionConverter<PlayBackgroundMusicActionContext, PlayBackgroundMusicAction> {

    // TODO: make parameter for this
    public static final double DEFAULT_BACKGROUND_MUSIC_VOLUME = 0.3;

    public void parseBackgroundResourceSection(BackgroundResourceSectionContext context, PlayBackgroundMusicAction action) {
        action.filename = this.toString(context.filename);
    }

    public void parseVolumeSection(VolumeSectionContext context, PlayBackgroundMusicAction action) {
        action.volume = this.toOptionalInt(context.volume);
    }

}
