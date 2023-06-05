package com.github.burningrain.lizard.editor.ui.window;

import com.github.burningrain.lizard.editor.api.ModalWindow;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.Consumer;


public class PluginIdChooserWindowController implements ModalWindow {

    public static final String FXML_PATH = "/fxml/plugin_id_chooser_window.fxml";

    @FXML
    private Label messageLabel;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private ListView<String> pluginIdsListView;


    public void init(Store store, String text, Consumer<String> okHandler, EventHandler<ActionEvent> cancelHandler) {
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(okButton, ButtonBar.ButtonData.RIGHT);

        messageLabel.setText(text);
        MultipleSelectionModel<String> selectionModel = pluginIdsListView.getSelectionModel();

        okButton.setOnAction(event -> okHandler.accept(selectionModel.getSelectedItem()));
        cancelButton.setOnAction(cancelHandler);

        pluginIdsListView.getItems().addAll(store.getAllPluginsTitles());

        selectionModel.selectFirst();
    }

}
