package com.programyourhome.adventureroom.module.immerse.dsl.converters;

import com.programyourhome.adventureroom.dsl.antlr.AbstractParseTreeAntlrActionConverter;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.PlayAudioActionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.ResourceSectionContext;
import com.programyourhome.adventureroom.module.immerse.dsl.ImmerseAdventureModuleParser.VolumeSectionContext;
import com.programyourhome.adventureroom.module.immerse.model.PlayAudioAction;

public class PlayAudioActionConverter extends AbstractParseTreeAntlrActionConverter<PlayAudioActionContext, PlayAudioAction> {

    @Override
    protected void registerRuleConverters() {
        this.registerRuleConverter(ResourceSectionContext.class, this::parseResourceSection);
        this.registerRuleConverter(VolumeSectionContext.class, this::parseVolumeSection);
    }

    private void parseResourceSection(ResourceSectionContext context, PlayAudioAction action) {
        action.filename = this.toString(context.filename);
    }

    private void parseVolumeSection(VolumeSectionContext context, PlayAudioAction action) {
        action.volume = this.toOptionalInt(context.volume);
    }

}
