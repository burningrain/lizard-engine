package com.github.burningrain.gvizfx;

import com.github.burningrain.gvizfx.element.NodeContainer;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public interface GraphEdgePart<A extends Node, E extends Node> extends NodeContainer<A> {

    void bindElement(E edgeNode, Node source, Node target);

    void unbindElement();

    void setStrokeWidth(Number newValue, Color color);

    void setColor(Paint newValue);

}
