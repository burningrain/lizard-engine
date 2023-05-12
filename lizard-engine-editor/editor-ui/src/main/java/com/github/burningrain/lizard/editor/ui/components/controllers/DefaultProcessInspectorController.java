package com.github.burningrain.lizard.editor.ui.components.controllers;

import com.github.burningrain.lizard.editor.ui.model.ProcessViewModelImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DefaultProcessInspectorController extends DefaultInspectorController<ProcessViewModelImpl<?,?>> {

    public static final String FXML_PATH = "/fxml/editor_header.fxml";

    @FXML
    private TextField textFieldTitle;

    @FXML
    private TextArea textAreaDescription;

    @Override
    protected void bindData(ProcessViewModelImpl<?, ?> currentElementViewModel) {
        textFieldTitle.textProperty().bindBidirectional(currentElementViewModel.processNameProperty());
        textAreaDescription.textProperty().bindBidirectional(currentElementViewModel.descriptionProperty());
    }

    @Override
    protected void unbindData(ProcessViewModelImpl<?, ?> currentElementViewModel) {
        textFieldTitle.textProperty().unbindBidirectional(currentElementViewModel.processNameProperty());
        textAreaDescription.textProperty().unbindBidirectional(currentElementViewModel.descriptionProperty());
    }

}
