package com.github.burningrain.gvizfx.element.edge.line;

import com.github.burningrain.gvizfx.element.edge.UserNodeEdgeElement;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;

public class UserNodePolylineElement<U extends Node> extends UserNodeEdgeElement<U, Polyline> {

    private InvalidationListener recalculateElementListener;

    public UserNodePolylineElement(U userNode) {
        super(userNode);
    }

    @Override
    public void bindElement(Polyline edge, Node source, Node target) {
        recalculateElementListener = observable -> {
            recalculateUserNode(edge);
        };
        edge.getPoints().addListener(recalculateElementListener);
        recalculateElementListener.invalidated(null);
    }

    @Override
    public void unbindElement() {

    }

    @Override
    public void setStrokeWidth(Number newValue, Color color) {

    }

    @Override
    public void setColor(Paint newValue) {

    }

    private void recalculateUserNode(Polyline edge) {
        ObservableList<Double> points = edge.getPoints();
        int size = points.size();
        if(size < 4) {
            return;
        }

        double prevEndY = points.get(size - 3);
        double prevEndX = points.get(size - 4);

        U userNode = getNode();
        userNode.setLayoutX(prevEndX);
        userNode.setLayoutY(prevEndY);
    }

}
