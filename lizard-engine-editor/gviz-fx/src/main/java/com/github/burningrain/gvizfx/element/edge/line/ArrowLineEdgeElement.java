package com.github.burningrain.gvizfx.element.edge.line;

import com.github.burningrain.gvizfx.element.edge.Arrow;
import com.github.burningrain.gvizfx.element.edge.ArrowEdgeElement;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class ArrowLineEdgeElement extends ArrowEdgeElement<Arrow, Line> {

    public ArrowLineEdgeElement() {
        super(new Arrow());
    }

    @Override
    public void bindElement(Line edgeNode, Node source, Node target) {
        Arrow arrow = getNode();

        arrow.startXProperty().bind(edgeNode.startXProperty());
        arrow.startYProperty().bind(edgeNode.startYProperty());

        arrow.endXProperty().bind(edgeNode.endXProperty());
        arrow.endYProperty().bind(edgeNode.endYProperty());
    }

    @Override
    public void unbindElement() {
        Arrow arrow = getNode();

        arrow.startXProperty().unbind();
        arrow.startYProperty().unbind();

        arrow.endXProperty().unbind();
        arrow.endYProperty().unbind();
    }

    @Override
    protected void setStrokeNodeWidth(Arrow node, double doubleValue, Color color) {
        node.setStrokeWidth(doubleValue);
        // fixme хак для перерисовки толщины стрелки. просто изменить strokeWidth недостаточно, увы
        node.strokeProperty().set(color);
    }

    @Override
    protected void setNodeColor(Arrow node, Paint newValue) {
        node.fillProperty().set(newValue);
        node.strokeProperty().set(newValue);
    }

}
