package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.antlr.AbstractReflectiveParseTreeAntlrActionConverter;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.PlayAudioActionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.ResourceSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.VolumeSectionContext;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction;

public class PlayAudioActionConverter extends AbstractReflectiveParseTreeAntlrActionConverter<PlayAudioActionContext, PlayAudioAction> {

    public void parseResourceSection(ResourceSectionContext context, PlayAudioAction action) {
        action.filename = this.toString(context.filename);
    }

    public void parseVolumeSection(VolumeSectionContext context, PlayAudioAction action) {
        action.volume = this.toOptionalInt(context.volume);
    }

}
