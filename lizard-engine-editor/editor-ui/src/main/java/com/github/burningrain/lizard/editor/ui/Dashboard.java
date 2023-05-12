package com.github.burningrain.lizard.editor.ui;

import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.burningrain.lizard.editor.ui.components.AccordionComponent;
import com.github.burningrain.lizard.editor.ui.components.InspectorComponent;
import com.github.burningrain.lizard.editor.ui.components.editor.ProcessEditorComponent;
import com.github.burningrain.lizard.editor.ui.core.StageManagerImpl;
import com.github.burningrain.lizard.editor.ui.io.ProjectModelImpl;
import com.github.burningrain.lizard.editor.ui.io.ProjectDescriptorImpl;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModelImpl;
import com.github.burningrain.lizard.editor.ui.model.Store;

import java.util.Map;

public class Dashboard extends StageManagerImpl {

    @Autowired
    private AccordionComponent accordionComponent;

    @Autowired
    private ProcessEditorComponent processEditorComponent;

    @Autowired
    private InspectorComponent inspectorComponent;

    @Autowired
    private Store store;

    private SplitPane splitPane;

    @Override
    protected void start(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lizard Editor");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        primaryStage.show();
    }

    @Override
    protected Node getMainNode() {
        BorderPane borderPane = new BorderPane();
        Node centerNode = createCenterNode();
        borderPane.setCenter(centerNode);

        return borderPane;
    }

    @Override
    public void activate() {
        accordionComponent.activate();
        processEditorComponent.activate();
        inspectorComponent.activate();
    }

    @Override
    public void afterActivation() {
        ProcessViewModelImpl processViewModel = new ProcessViewModelImpl();
        for (Map.Entry<String, ProcessPropertiesInspectorBinder> entry : store.getProcessPropertyBinders().entrySet()) {
            processViewModel.putDatum(entry.getKey(), entry.getValue().createNewNodeModel());
        }
        store.setCurrentProjectModel(new ProjectModelImpl(new ProjectDescriptorImpl(), processViewModel));
    }

    private Node createCenterNode() {
        splitPane = new SplitPane();
        splitPane.setDividerPositions(0.10, 0.70, 0.20);

        splitPane.getItems().add(accordionComponent.getNode());
        splitPane.getItems().add(processEditorComponent.getNode());
        splitPane.getItems().add(inspectorComponent.getNode());

        return splitPane;
    }

}
