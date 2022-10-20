package com.github.burningrain.lizard.editor.ui.components.editor.mode.states;

import com.github.burningrain.gvizfx.handlers.GraphViewBindersContainer;
import com.github.burningrain.gvizfx.handlers.binders.VertexHandlersBinder;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.states.binders.CreateEdgeBinder;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;

public class CreateEdgeFsmState extends EditorFsmState {

    public CreateEdgeFsmState(ActionManager actionManager, ActionFactory actionFactory) {
        super(actionManager, actionFactory);
    }

    @Override
    public void apply(GraphViewBindersContainer bindersContainer) {
        if(bindersContainer.isActive(VertexHandlersBinder.class)) {
            bindersContainer.cancel(VertexHandlersBinder.class);
        }

        if(!bindersContainer.isRegistered(CreateEdgeBinder.class)) {
            bindersContainer.registerBinderAndApply(new CreateEdgeBinder(getActionManager(), getActionFactory()));
        } else {
            bindersContainer.apply(CreateEdgeBinder.class);
        }
    }

    @Override
    public void cancel(GraphViewBindersContainer bindersContainer) {
        bindersContainer.cancel(CreateEdgeBinder.class);
        bindersContainer.apply(VertexHandlersBinder.class);
    }

}
