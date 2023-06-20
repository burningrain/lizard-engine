package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Scope("prototype")
@Component
public class ShowAboutLizardEditorAction implements NotRevertAction {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private UiUtils uiUtils;

    @Override
    public String getId() {
        return Actions.SHOW_ABOUT_LIZARD_EDITOR;
    }

    @Override
    public void execute() {
        List<PluginDescriptor> descriptors = pluginManager.getStartedPlugins().stream().map(PluginWrapper::getDescriptor).collect(Collectors.toList());

        //todo вынести это отсюда
        VBox pane = new VBox();
        descriptors.forEach(pluginDescriptor -> {
            Button button = new Button(pluginDescriptor.getPluginId());
            button.setOnAction(event -> {
                StringBuilder builder = new StringBuilder();
                builder
                        .append("pluginId: ").append(pluginDescriptor.getPluginId()).append("\n")
                        .append("version: ").append(pluginDescriptor.getVersion()).append("\n")
                        .append("description: ").append(pluginDescriptor.getPluginDescription()).append("\n")
                        .append("license: ").append(pluginDescriptor.getLicense()).append("\n")
                        .append("provider: ").append(pluginDescriptor.getProvider()).append("\n")
                        .append("plugin class: ").append(pluginDescriptor.getPluginClass()).append("\n")
                        .append("requires: ").append(pluginDescriptor.getRequires()).append("\n")
                        ;

                Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
                alertDialog.setHeaderText(pluginDescriptor.getPluginId());
                alertDialog.setContentText(builder.toString());
                Optional<ButtonType> result = alertDialog.showAndWait();
            });

            pane.getChildren().add(button);
        });

        uiUtils.createStageChild(stage -> {
            Stage modalWindow = new Stage();
            modalWindow.setResizable(false);
            modalWindow.setTitle("About");
            modalWindow.setScene(new Scene(pane));
            modalWindow.initModality(Modality.WINDOW_MODAL);
            modalWindow.initOwner(stage);
            Platform.runLater(modalWindow::show);
        });
    }

}
