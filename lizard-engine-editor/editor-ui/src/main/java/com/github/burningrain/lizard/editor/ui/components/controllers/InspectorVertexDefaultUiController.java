package com.github.burningrain.lizard.editor.ui.components.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import com.github.burningrain.lizard.editor.ui.model.ProcessElementType;
import com.github.burningrain.lizard.editor.ui.model.VertexViewModel;

public class InspectorVertexDefaultUiController extends DefaultInspectorController<VertexViewModel> {

    @FXML
    private Label labelType;

    @FXML
    private Label labelPluginId;

    @FXML
    private Label labelX;

    @FXML
    private Label labelY;

    @FXML
    private TextField textFieldX;

    @FXML
    private TextField textFieldY;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label classnameLabel;

    protected void bindData(VertexViewModel currentElementViewModel) {
        ProcessElementType type = currentElementViewModel.getType();
        labelType.textProperty().setValue(type.getElementName());
        labelPluginId.textProperty().setValue(type.getPluginId());

        Bindings.bindBidirectional(
                textFieldX.textProperty(),
                currentElementViewModel.xProperty(),
                new NumberStringConverter()
        );
        Bindings.bindBidirectional(
                textFieldY.textProperty(),
                currentElementViewModel.yProperty(),
                new NumberStringConverter()
        );
        descriptionTextArea.textProperty().bindBidirectional(currentElementViewModel.descriptionProperty());
        classnameLabel.textProperty().bindBidirectional(currentElementViewModel.classNameProperty());
    }

    protected void unbindData(VertexViewModel vertexModel) {
        Bindings.unbindBidirectional(textFieldX.textProperty(), vertexModel.xProperty());
        Bindings.unbindBidirectional(textFieldY.textProperty(), vertexModel.yProperty());
        descriptionTextArea.textProperty().unbindBidirectional(vertexModel.descriptionProperty());
        classnameLabel.textProperty().unbindBidirectional(vertexModel.classNameProperty());

        labelType.textProperty().set("");
        textFieldX.setText("");
        textFieldY.setText("");
        descriptionTextArea.setText("");
        classnameLabel.setText("");
    }

}
