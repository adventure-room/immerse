package com.programyourhome.adventureroom.immerse.executor;

import com.programyourhome.adventureroom.immerse.module.ImmerseAdventureModule;
import com.programyourhome.adventureroom.immerse.service.Immerse;
import com.programyourhome.adventureroom.model.script.action.Action;
import com.programyourhome.iotadventure.runner.action.executor.ActionExecutor;
import com.programyourhome.iotadventure.runner.context.ExecutionContext;

public abstract class AbstractImmerseExecutor<A extends Action> implements ActionExecutor<A> {

    protected ImmerseAdventureModule getModule(ExecutionContext context) {
        return context.getModule(ImmerseAdventureModule.ID);
    }

    protected Immerse getImmerse(ExecutionContext context) {
        return this.getModule(context).getImmerse();
    }

}
