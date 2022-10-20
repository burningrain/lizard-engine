package com.github.burningrain.gvizfx.element.edge.line;

import com.github.burningrain.gvizfx.element.EdgeElement;
import com.github.burningrain.gvizfx.element.edge.ArrowPolylineEdgeElement;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polyline;

public class PolylineSelfEdgeElement extends EdgeElement<Polyline, ArrowPolylineEdgeElement> {

    public PolylineSelfEdgeElement(String edgeId, boolean isDirectional) {
        this(edgeId, isDirectional, null);
    }

    public PolylineSelfEdgeElement(String edgeId, boolean isDirectional, Node userNode) {
        super(
                edgeId,
                isDirectional,
                new Polyline(),
                () -> new ArrowPolylineEdgeElement(),
                userNode == null? null : new UserNodePolylineElement(userNode)
        );
    }


    @Override
    protected void setColor(Polyline edge, Paint color) {
        edge.strokeProperty().set(color);
    }

    @Override
    protected void setStrokeWidth(Polyline edge, double newValue) {
        edge.setStrokeWidth(newValue);
    }

    @Override
    protected void selectEdge(Polyline edge, Paint color) {
        edge.strokeProperty().set(color);
    }

    @Override
    protected void deselectEdge(Polyline edge, Paint color) {
        edge.strokeProperty().set(color);
    }

    @Override
    protected void recalculateElement(Node source, Node target) {
        Polyline polyline = getEdge();

        polyline.getPoints().clear();

        double width = source.getBoundsInLocal().getWidth();
        double height = source.getBoundsInLocal().getHeight();

        double startLeftX = source.getLayoutX();
        double startRightX = startLeftX + width;
        double startTopY = source.getLayoutY();
        double startBottomY = startTopY + height;

        polyline.getPoints().addAll(
                startLeftX + width * 3 / 4, startTopY,
                startLeftX + width * 3 / 4, startTopY - height / 4,
                startRightX + width / 2, startTopY - height / 4,
                startRightX + width / 2, startTopY + height / 4,
                startRightX, startTopY + height / 4
        );
    }

}
