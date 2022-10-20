package com.github.burningrain.lizard.editor.ui.components.editor;

import com.github.burningrain.gvizfx.element.VertexElement;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GraphVertexWrapper extends VertexElement<VBox> {

    private Label labelId;

    private final VBox vBox = new VBox();

    public GraphVertexWrapper(String elementId, Node node) {
        super(elementId, createVBox(node));
        labelId = new Label();
        vBox.getChildren().add(labelId);

        if(node != null) {
            vBox.getChildren().add(node);
        }
    }

    private static VBox createVBox(Node node) {
        return null;
    }

    @Override
    public VBox copyNodeForMinimap() {
        return null;
    }

}
