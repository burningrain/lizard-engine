package com.github.burningrain.lizard.editor.ui.components.editor.mode.states.binders;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;
import com.github.burningrain.gvizfx.input.DefaultActions;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.lizard.editor.ui.actions.impl.DeleteCurrentElementFromEditorAction;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class DeleteElementFilterBinder implements GraphHandlersBinder {

    private ActionManager actionManager;
    private ActionFactory actionFactory;

    private EventHandler<KeyEvent> onKeyPressedEventFilter;

    public DeleteElementFilterBinder(ActionManager actionManager, ActionFactory actionFactory) {
        this.actionManager = actionManager;
        this.actionFactory = actionFactory;
    }

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        onKeyPressedEventFilter = keyEvent -> {
            if (inputEventManager.match(DefaultActions.DELETE_GRAPH_ELEMENT, keyEvent)) {
                DeleteCurrentElementFromEditorAction action = actionFactory.createAction(DeleteCurrentElementFromEditorAction.class);
                actionManager.executeAction(action);
            }
        };

        viewData.getScrollPane().addEventFilter(KeyEvent.KEY_PRESSED, onKeyPressedEventFilter);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        viewData.getScrollPane().removeEventFilter(KeyEvent.KEY_PRESSED, onKeyPressedEventFilter);
    }

}
