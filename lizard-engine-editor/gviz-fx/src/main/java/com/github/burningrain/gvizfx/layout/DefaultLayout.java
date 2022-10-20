package com.github.burningrain.gvizfx.layout;

import javafx.scene.Node;

import java.util.Collection;

public class DefaultLayout implements Layout {

    public void lay(Collection<Node> vertices) {
        for (Node vertex : vertices) {
            vertex.relocate(vertex.getLayoutX(), vertex.getLayoutY());
        }
    }

}