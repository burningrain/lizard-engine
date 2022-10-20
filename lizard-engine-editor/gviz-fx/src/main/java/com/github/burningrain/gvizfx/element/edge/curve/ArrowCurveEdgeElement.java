package com.github.burningrain.gvizfx.element.edge.curve;

import com.github.burningrain.gvizfx.element.edge.Arrow;
import com.github.burningrain.gvizfx.element.edge.ArrowEdgeElement;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;

public class ArrowCurveEdgeElement extends ArrowEdgeElement<Arrow, Path>  {

    public ArrowCurveEdgeElement() {
        super(new Arrow());
    }

    @Override
    public void bindElement(Path edgeNode, Node source, Node target) {
        QuadCurveTo quadCurveTo = (QuadCurveTo) edgeNode.getElements().get(1);
        Arrow arrow = getNode();

        quadCurveTo.controlXProperty().addListener((observable, oldValue, newValue) -> {
            int diff = 100;
            double stepX = (target.getLayoutX() - newValue.doubleValue()) / diff;
            arrow.startXProperty().set(target.getLayoutX() - stepX);
        });
        quadCurveTo.controlYProperty().addListener((observable, oldValue, newValue) -> {
            int diff = 100;
            double stepY = (target.getLayoutY() - newValue.doubleValue()) / diff;
            arrow.startYProperty().set(target.getLayoutY() - stepY);
        });

        arrow.endXProperty().bind(target.layoutXProperty());
        arrow.endYProperty().bind(target.layoutYProperty());
    }

    @Override
    public void unbindElement() {

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
    }

}
