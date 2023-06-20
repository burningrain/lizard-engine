package com.github.burningrain.lizard.editor.ui.components.editor;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.ui.core.UiComponent;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.ui.draggers.VertexDragAndDrop;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProcessEditorComponent implements UiComponent<TabPane> {

    @Autowired
    private GraphView graphView;

    @Autowired
    private Store store;

    @Autowired
    private ActionManager actionManager;

    @Autowired
    private ActionFactory actionFactory;

    @Autowired
    private VertexDragAndDrop vertexDragAndDrop;

    private TabPaneComponent tabPaneComponent;

    @PostConstruct
    public void init() {
        tabPaneComponent = new TabPaneComponent(graphView, store, actionManager, actionFactory, vertexDragAndDrop);
    }

    @Override
    public void activate() {
        tabPaneComponent.activate();
    }

    @Override
    public void deactivate() {
        tabPaneComponent.deactivate();
        store.currentProjectModelProperty().set(null);
    }

    @Override
    public TabPane getNode() {
        return tabPaneComponent.getNode();
    }

}
