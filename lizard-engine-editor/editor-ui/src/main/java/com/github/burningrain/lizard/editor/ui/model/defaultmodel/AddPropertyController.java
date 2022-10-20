package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddPropertyController {

    @FXML
    private VBox root;

    @FXML
    private TextField keyTextField;

    @FXML
    private TextField valueTextField;

    @FXML
    private CheckBox showCheckBox;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    public void handleClick(ClickHandler clickHandler) {
        okButton.setOnAction(event -> {
            clickHandler.onClickOK(this);
        });
        cancelButton.setOnAction(event -> {
            clickHandler.onClickCancel(this);
        });
    }

    public VBox getRoot() {
        return root;
    }

    public TextField getKeyTextField() {
        return keyTextField;
    }

    public TextField getValueTextField() {
        return valueTextField;
    }

    public CheckBox getShowCheckBox() {
        return showCheckBox;
    }

    public Button getOkButton() {
        return okButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public interface ClickHandler {

        void onClickOK(AddPropertyController controller);

        void onClickCancel(AddPropertyController controller);

    }

}
