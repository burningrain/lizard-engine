package com.github.burningrain.lizard.editor.ui.components.editor.mode;

import com.github.burningrain.gvizfx.handlers.GraphViewBindersContainer;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.states.CreateEdgeFsmState;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.states.EditorFsmState;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.states.NoneFsmState;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.states.SelectionFsmState;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.states.binders.DeleteElementFilterBinder;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.ui.draggers.VertexDragAndDrop;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProcessEditorFsmImpl implements ProcessEditorFsm {

    private GraphViewBindersContainer bindersContainer;

    private States currentStateEnum;
    private EditorFsmState currentFsmState;

    private Map<States, EditorFsmState> containers;

    public ProcessEditorFsmImpl(GraphViewBindersContainer bindersContainer, ActionManager actionManager, ActionFactory actionFactory, VertexDragAndDrop vertexDragAndDrop) {
        this.bindersContainer = bindersContainer;
        init(actionManager, actionFactory, vertexDragAndDrop);
    }

    private void init(ActionManager actionManager, ActionFactory actionFactory, VertexDragAndDrop vertexDragAndDrop) {
        initPersistBinders(actionManager, actionFactory, vertexDragAndDrop);
        initContainers(actionManager, actionFactory);
        changeState(States.NONE);
    }

    @Override
    public States getCurrentState() {
        return currentStateEnum;
    }

    @Override
    public void changeState(States newState) {
        EditorFsmState mouseHandlersContainer = containers.get(newState);
        Objects.requireNonNull(mouseHandlersContainer, "EditorFsmState must not be null. State=[" + newState + "]");

        this.currentStateEnum = newState;
        switchMouseGestures(mouseHandlersContainer);
    }

    @Override
    public void dispose() {
        currentFsmState.cancel(bindersContainer);
    }

    private void initPersistBinders(ActionManager actionManager, ActionFactory actionFactory, VertexDragAndDrop vertexDragAndDrop) {
        bindersContainer.registerBinderAndApply(new DeleteElementFilterBinder(actionManager, actionFactory));
        bindersContainer.registerBinderAndApply(vertexDragAndDrop);
    }

    private void initContainers(ActionManager actionManager, ActionFactory actionFactory) {
        HashMap<States, EditorFsmState> map = new HashMap<>();
        map.put(States.NONE, new NoneFsmState(actionManager, actionFactory));
        map.put(States.SELECT_GRAPH_ELEMENT, new SelectionFsmState(actionManager, actionFactory));
        map.put(States.CREATE_TRANSITION, new CreateEdgeFsmState(actionManager, actionFactory));

        this.containers = Collections.unmodifiableMap(map);

        this.currentFsmState = map.get(States.NONE);
    }

    private void switchMouseGestures(EditorFsmState newContainer) {
        this.currentFsmState.cancel(bindersContainer);
        this.currentFsmState = newContainer;
        newContainer.apply(bindersContainer);
    }

}
