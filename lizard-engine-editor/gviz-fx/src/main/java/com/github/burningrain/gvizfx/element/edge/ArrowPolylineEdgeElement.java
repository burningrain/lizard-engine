package com.github.burningrain.gvizfx.element.edge;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;

public class ArrowPolylineEdgeElement extends ArrowEdgeElement<Arrow, Polyline> {

    private InvalidationListener listener;
    private Polyline edgeNode;

    public ArrowPolylineEdgeElement() {
        super(new Arrow());
    }

    @Override
    public void bindElement(Polyline edgeNode, Node source, Node target) {
        this.edgeNode = edgeNode;
        ObservableList<Double> points = edgeNode.getPoints();
        listener = observable -> {
            int size = points.size();
            if(size < 4) {
                return;
            }

            double endY = points.get(size - 1);
            double endX = points.get(size - 2);
            double prevEndY = points.get(size - 3);
            double prevEndX = points.get(size - 4);

            Arrow arrow = getNode();
            arrow.setStartX(prevEndX);
            arrow.setStartY(prevEndY);
            arrow.setEndX(endX);
            arrow.setEndY(endY);
        };

        points.addListener(listener);
        listener.invalidated(null);
    }

    @Override
    public void unbindElement() {
        ObservableList<Double> points = edgeNode.getPoints();
        points.removeListener(listener);

        this.edgeNode = null;
        this.listener = null;
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
