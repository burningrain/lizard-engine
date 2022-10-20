package com.github.burningrain.gvizfx.element.edge;

import com.github.burningrain.gvizfx.GraphEdgePart;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class ArrowEdgeElement<A extends Node, E extends Node> implements GraphEdgePart<A, E> {

    private final A node;

    @Override
    public A getNode() {
        return node;
    }

    public ArrowEdgeElement(A node) {
        this.node = node;
    }

    public abstract void bindElement(E edgeNode, Node source, Node target);

    public abstract void unbindElement();

    public void setStrokeWidth(Number newValue, Color color) {
        setStrokeNodeWidth(node, newValue.doubleValue(), color);
    }

    public void setColor(Paint newValue) {
        setNodeColor(node, newValue);
    }

    protected abstract void setStrokeNodeWidth(A node, double doubleValue, Color color);

    protected abstract void setNodeColor(A node, Paint newValue);

}
