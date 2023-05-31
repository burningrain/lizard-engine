package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DefaultGraphElementNode extends VBox {

    private final Label label;

    public DefaultGraphElementNode() {
        label = new Label();
        getChildren().add(label);
    }

    public Label getLabel() {
        return label;
    }

}
