package com.github.burningrain.gvizfx.element;

import javafx.scene.Node;

public abstract class VertexElement<N extends Node> extends GraphElement<N> {

    public VertexElement(String graphElementId, N node) {
        super(graphElementId, node);
    }

    public abstract N copyNodeForMinimap();

}
