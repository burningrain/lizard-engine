package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex;

import com.badlogic.gdx.graphics.g2d.Animation;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

public class VertexInspectorController {

    public static final String PATH = "/vertex_inspector.fxml";

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField fromTextField;

    @FXML
    private TextField toTextField;

    @FXML
    private TextField frameFrequencyTextField;

    @FXML
    private ComboBox<Animation.PlayMode> modeComboBox;

    @FXML
    private CheckBox loopingCheckbox;

    @FXML
    private GridPane gridPane;

    private ChangeListener<Animation.PlayMode> selectedItemListener;

    public GridPane getPane() {
        return gridPane;
    }

    public void bindModel(SimpleAnimationVertexModel model) {
        nameTextField.textProperty().bindBidirectional(model.nameProperty());
        Bindings.bindBidirectional(fromTextField.textProperty(), model.fromProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(toTextField.textProperty(), model.toProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(frameFrequencyTextField.textProperty(), model.frameFrequencyProperty(), new NumberStringConverter());
        loopingCheckbox.selectedProperty().bindBidirectional(model.loopingProperty());

        // mode
        modeComboBox.getItems().addAll(Animation.PlayMode.values());
        selectedItemListener = (observable, oldValue, newValue) -> model.setMode(newValue);
        modeComboBox.getSelectionModel().selectedItemProperty().addListener(selectedItemListener);
        modeComboBox.getSelectionModel().select(model.getMode());
    }

    public void unbindModel(SimpleAnimationVertexModel model) {
        nameTextField.textProperty().unbindBidirectional(model.nameProperty());
        Bindings.unbindBidirectional(fromTextField.textProperty(), model.fromProperty());
        Bindings.unbindBidirectional(toTextField.textProperty(), model.toProperty());
        Bindings.unbindBidirectional(frameFrequencyTextField.textProperty(), model.frameFrequencyProperty());
        loopingCheckbox.selectedProperty().unbindBidirectional(model.loopingProperty());
        // mode
        modeComboBox.getSelectionModel().selectedItemProperty().removeListener(selectedItemListener);
        selectedItemListener = null;
    }

}
