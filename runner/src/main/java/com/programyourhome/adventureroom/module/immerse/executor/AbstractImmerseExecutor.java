package com.programyourhome.adventureroom.module.immerse.executor;

import java.util.Collection;

import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.script.action.Action;
import com.programyourhome.adventureroom.module.immerse.model.SpeakerExternalResource;
import com.programyourhome.adventureroom.module.immerse.module.ImmerseAdventureModule;
import com.programyourhome.adventureroom.module.immerse.service.Immerse;
import com.programyourhome.immerse.domain.speakers.Speaker;
import com.programyourhome.iotadventure.runner.action.executor.ActionExecutor;

import one.util.streamex.StreamEx;

public abstract class AbstractImmerseExecutor<A extends Action> implements ActionExecutor<A> {

    protected ImmerseAdventureModule getModule(ExecutionContext context) {
        return context.getModule(ImmerseAdventureModule.ID);
    }

    protected Immerse getImmerse(ExecutionContext context) {
        return this.getModule(context).getImmerse();
    }

    protected Collection<Integer> getSpeakerIds(ExecutionContext context) {
        return StreamEx.of(context.getAdventure().getExternalResources(SpeakerExternalResource.class))
                .map(Speaker::getId)
                .toList();
    }

}
