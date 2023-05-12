package com.github.burningrain.lizard.editor.ui.components.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.github.burningrain.lizard.editor.api.project.model.EdgeViewModel;
import com.github.burningrain.lizard.editor.api.project.model.ProcessElementType;

public class InspectorEdgeDefaultUiController extends DefaultInspectorController<EdgeViewModel> {

    public static final String FXML_PATH = "/fxml/inspector_edge.fxml";

    @FXML
    private Label labelType;

    @FXML
    private Label labelPluginId;

    @FXML
    private TextField tagTextField;

    @FXML
    private TextArea descriptionTextArea;

    @Override
    protected void bindData(EdgeViewModel currentElementViewModel) {
        ProcessElementType type = currentElementViewModel.getType();
        labelType.textProperty().setValue(type.getElementName());
        labelPluginId.textProperty().setValue(type.getPluginId());

        descriptionTextArea.textProperty().bindBidirectional(currentElementViewModel.descriptionProperty());
        tagTextField.textProperty().bindBidirectional(currentElementViewModel.tagProperty());
    }

    @Override
    protected void unbindData(EdgeViewModel edgeModel) {
        descriptionTextArea.textProperty().unbindBidirectional(edgeModel.descriptionProperty());
        tagTextField.textProperty().unbindBidirectional(edgeModel.tagProperty());

        labelType.textProperty().set("");
        descriptionTextArea.setText("");
    }

}
