package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class DefaultGraphElementNode extends Pane {

    private Label label;

    public DefaultGraphElementNode() {
        label = new Label();
        getChildren().add(label);
    }

    public Label getLabel() {
        return label;
    }

}
