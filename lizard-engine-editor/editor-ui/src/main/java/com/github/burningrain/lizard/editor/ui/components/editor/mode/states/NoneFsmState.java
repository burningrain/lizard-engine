package com.github.burningrain.lizard.editor.ui.components.editor.mode.states;

import com.github.burningrain.gvizfx.handlers.GraphViewBindersContainer;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;

public class NoneFsmState extends EditorFsmState {

    public NoneFsmState(ActionManager actionManager, ActionFactory actionFactory) {
        super(actionManager, actionFactory);
    }

    @Override
    public void apply(GraphViewBindersContainer bindersContainer) {

    }

    @Override
    public void cancel(GraphViewBindersContainer bindersContainer) {

    }

}
