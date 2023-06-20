package com.github.burningrain.lizard.editor.ui.components.editor;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;
import com.github.burningrain.lizard.editor.ui.core.UiComponent;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.ui.draggers.VertexDragAndDrop;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.HashSet;
import java.util.Objects;


public class TabPaneComponent implements UiComponent<TabPane> {

    private final GraphView graphView;
    private final Store store;
    private final ActionManager actionManager;
    private final ActionFactory actionFactory;
    private final VertexDragAndDrop vertexDragAndDrop;

    private final TabPane tabPane = new TabPane();

    private final HashSet<String> indices = new HashSet<>();
    private GraphEditorComponent currentComponent;
    private String currentTabId;

    private final ChangeListener<ProjectModel> projectModelChangeListener = new ChangeListener<ProjectModel>() {
        @Override
        public void changed(ObservableValue<? extends ProjectModel> observable, ProjectModel oldValue, ProjectModel newValue) {
            if (newValue == null) {
                return;
            }

            String title = newValue.getDescriptor().getTitle();
            String oldTitle = null;
            if(oldValue != null) {
                oldTitle = oldValue.getDescriptor().getTitle();
            }

            if(oldTitle != null) {
                deactivatePrevProjectTab(oldTitle);
            }

            Tab currentTab;
            // создали новый проект
            if (!indices.contains(title)) {
                indices.add(title);

                currentTab = createTab(title, currentComponent.getNode());
                tabPane.getTabs().add(currentTab);
            } else {
                // переоткрываем старый
                currentTab = getTabById(title);
                currentTab.setContent(currentComponent.getNode());
            }

            store.currentProjectModelProperty().removeListener(projectModelChangeListener);
            tabPane.getSelectionModel().select(currentTab);
            store.currentProjectModelProperty().addListener(projectModelChangeListener);

            currentTabId = title;
            currentComponent.changeProjectModel(oldValue, newValue);
        }
    };

    private Tab getTabById(String tabId) {
        Objects.requireNonNull(tabId);
        return tabPane.getTabs().stream().filter(tab -> tabId.equals(tab.getId())).findFirst().orElse(null);
    }

    private void deactivatePrevProjectTab(String tabId) {
        Tab tab = getTabById(tabId);
        if(tab != null) {
            tab.setContent(null);
        }
    }

    private final EventHandler<Event> selectionHandler = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            Tab tab = (Tab) event.getSource();
            String processName = store.getCurrentProjectModel().getProcessViewModel().getProcessName();

            if (tab.getId().equals(processName)) {
                return;
            }

            store.changeCurrentProject(tab.getId());
        }
    };

    public TabPaneComponent(GraphView graphView, Store store, ActionManager actionManager,
                            ActionFactory actionFactory, VertexDragAndDrop vertexDragAndDrop) {
        this.graphView = graphView;
        this.store = store;
        this.actionManager = actionManager;
        this.actionFactory = actionFactory;
        this.vertexDragAndDrop = vertexDragAndDrop;

        this.currentComponent = createCurrentComponent(); // отображение графа
    }

    @Override
    public void activate() {
        store.currentProjectModelProperty().addListener(projectModelChangeListener);
    }

    @Override
    public void deactivate() {
    }

    @Override
    public TabPane getNode() {
        return tabPane;
    }

    public void deleteProject(String tabId) {
        indices.remove(tabId);
        store.getProjectModels().remove(tabId);
    }

    private Tab createTab(String tabId, Node content) {
        Tab tab = new Tab();
        tab.setId(tabId);
        tab.setText(tabId);
        tab.setContent(content);
        tab.setOnClosed(event -> {
            Tab t = (Tab) event.getSource();
            deleteProject(t.getId());
        });
        tab.setOnSelectionChanged(selectionHandler);

        return tab;
    }

    private GraphEditorComponent createCurrentComponent() {
        GraphEditorComponent currentComponent = new GraphEditorComponent(graphView, store, actionManager, actionFactory, vertexDragAndDrop);
        currentComponent.activate();
        return currentComponent;
    }

}
