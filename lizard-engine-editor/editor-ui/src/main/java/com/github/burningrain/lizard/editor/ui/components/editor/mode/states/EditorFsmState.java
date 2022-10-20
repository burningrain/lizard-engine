package com.github.burningrain.lizard.editor.ui.components.editor.mode.states;

import com.github.burningrain.gvizfx.handlers.GraphViewBindersContainer;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;

public abstract class EditorFsmState {

    private final ActionManager actionManager;
    private final ActionFactory actionFactory;

    public EditorFsmState(ActionManager actionManager, ActionFactory actionFactory) {
        this.actionManager = actionManager;
        this.actionFactory = actionFactory;
    }

    public abstract void apply(GraphViewBindersContainer bindersContainer);

    public abstract void cancel(GraphViewBindersContainer bindersContainer);

    protected ActionManager getActionManager() {
        return actionManager;
    }

    protected ActionFactory getActionFactory() {
        return actionFactory;
    }

}
