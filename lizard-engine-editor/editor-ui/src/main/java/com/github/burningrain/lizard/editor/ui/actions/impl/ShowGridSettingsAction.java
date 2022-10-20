package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.components.controllers.GridSettingsController;
import com.github.burningrain.lizard.editor.ui.utils.FxUtils;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class ShowGridSettingsAction implements NotRevertAction {

    @Autowired
    private GraphView graphView;

    @Autowired
    private UiUtils uiUtils;

    private GridSettingsController gridSettingsController;

    @Override
    public String getId() {
        return Actions.SHOW_GRID_SETTINGS;
    }

    @Override
    public void execute() {
        gridSettingsController = new GridSettingsController();
        Parent gridSettings = FxUtils.loadFxml(gridSettingsController, "/fxml/grid_settings.fxml");
        gridSettingsController.bind(graphView);

        Scene scene = new Scene(gridSettings);
        uiUtils.createStageChild(stage -> {
            Stage modalWindow = new Stage();
            modalWindow.setResizable(false);
            modalWindow.setTitle("Grid Settings");
            modalWindow.setScene(scene);
            modalWindow.initModality(Modality.WINDOW_MODAL);
            modalWindow.initOwner(stage);
            Platform.runLater(modalWindow::show);
        });
        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    private void closeWindowEvent(WindowEvent event) {
        gridSettingsController.unbind();

        //event.consume();
    }

}
