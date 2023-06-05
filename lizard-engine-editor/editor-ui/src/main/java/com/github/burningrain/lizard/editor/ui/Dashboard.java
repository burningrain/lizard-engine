package com.github.burningrain.lizard.editor.ui;

import com.github.burningrain.lizard.editor.ui.actions.impl.LoadPluginDataToStoreAction;
import com.github.burningrain.lizard.editor.ui.components.AccordionComponent;
import com.github.burningrain.lizard.editor.ui.components.InspectorComponent;
import com.github.burningrain.lizard.editor.ui.components.editor.ProcessEditorComponent;
import com.github.burningrain.lizard.editor.ui.core.StageManagerImpl;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

public class Dashboard extends StageManagerImpl {

    @Autowired
    private AccordionComponent accordionComponent;

    @Autowired
    private ProcessEditorComponent processEditorComponent;

    @Autowired
    private InspectorComponent inspectorComponent;

    @Autowired
    private Store store;

    @Autowired
    private ActionManager actionManager;

    @Autowired
    private ActionFactory actionFactory;

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
    }

    @Override
    public void afterActivation() {
        actionManager.executeAction(actionFactory.createAction(LoadPluginDataToStoreAction.class)); //todo вынести отсюда куда-нить в инициализацию

        accordionComponent.activate();
        processEditorComponent.activate();
        inspectorComponent.activate();
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
