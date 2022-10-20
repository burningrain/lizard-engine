package com.github.burningrain.gvizfx.element.edge;

import com.github.burningrain.gvizfx.GraphEdgePart;
import javafx.scene.Node;

import java.util.Objects;

public abstract class UserNodeEdgeElement<U extends Node, E extends Node> implements GraphEdgePart<U, E> {

    private final U node;

    public UserNodeEdgeElement(U node) {
        this.node = Objects.requireNonNull(node);
    }

    @Override
    public U getNode() {
        return node;
    }

}
