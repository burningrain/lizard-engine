package com.github.burningrain.lizard.editor.ui.components.editor.mode.states.binders;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.input.GraphViewAction;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.element.VertexElement;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;
import com.github.burningrain.lizard.editor.ui.actions.impl.AddEdgeToEditorAction;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class CreateEdgeBinder implements GraphHandlersBinder {

    private final ActionManager actionManager;
    private final ActionFactory actionFactory;

    private VertexElement firstVertex;

    private Consumer<MouseEvent> onMouseClickedUserHandler;

    private EventHandler<KeyEvent> cancelCreateEdgeEventHandler;
    private static final String CANCEL_CREATE_EDGE = "CANCEL CREATE EDGE";
    private GraphViewAction cancelCreateEdgeAction;

    public CreateEdgeBinder(ActionManager actionManager, ActionFactory actionFactory) {
        this.actionManager = actionManager;
        this.actionFactory = actionFactory;
    }

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        onMouseClickedUserHandler = event -> {
            Object source = event.getSource();
            if (source instanceof VertexElement) {
                VertexElement vertex = (VertexElement) source;
                if (firstVertex == null) {
                    firstVertex = vertex;
                    firstVertex.select();
                } else {
                    AddEdgeToEditorAction action = actionFactory.createAction(AddEdgeToEditorAction.class);
                    action.setSource(firstVertex.getGraphElementId());
                    action.setTarget(vertex.getGraphElementId());
                    actionManager.executeAction(action);

                    firstVertex.deselect();
                    firstVertex = null;
                }
            }
        };
        handlers.getVertexHandlers().getOnMouseClickedHandler().addListener(onMouseClickedUserHandler);

        inputEventManager.register(CANCEL_CREATE_EDGE, cancelCreateEdgeAction = new GraphViewAction() {
            @Override
            public List<Class<? extends InputEvent>> getSupportedEvents() {
                return Collections.singletonList(KeyEvent.class);
            }

            @Override
            public boolean test(InputEvent inputEvent) {
                KeyEvent keyEvent = (KeyEvent) inputEvent;
                return KeyCode.ESCAPE == keyEvent.getCode();
            }
        });
        viewData.getScrollPane().addEventFilter(KeyEvent.KEY_PRESSED, cancelCreateEdgeEventHandler = event -> {
            if(!inputEventManager.match(CANCEL_CREATE_EDGE, event)) {
                return;
            }

            if(firstVertex != null) {
                firstVertex.deselect();
                firstVertex = null;
            }
        });
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        handlers.getVertexHandlers().getOnMouseClickedHandler().removeListener(onMouseClickedUserHandler);
        viewData.getScrollPane().removeEventFilter(KeyEvent.KEY_PRESSED, cancelCreateEdgeEventHandler);
        inputEventManager.unregister(CANCEL_CREATE_EDGE);
        cancelCreateEdgeAction = null;
        cancelCreateEdgeEventHandler = null;
        onMouseClickedUserHandler = null;
    }

}
