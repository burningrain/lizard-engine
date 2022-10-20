package com.github.burningrain.gvizfx.element.edge.line;

import com.github.burningrain.gvizfx.Intersector;
import com.github.burningrain.gvizfx.element.EdgeElement;
import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class LineEdgeElement extends EdgeElement<Line, ArrowLineEdgeElement> {
    public LineEdgeElement(String graphElementId,
                           boolean isDirectional,
                           Node userNode) {
        super(
                graphElementId,
                isDirectional,
                new Line(),
                () -> new ArrowLineEdgeElement(),
                userNode == null? null : new UserNodeLineElement(userNode)
        );
    }

    @Override
    protected void setColor(Line edge, Paint color) {
        edge.setStroke(color);
    }

    @Override
    protected void setStrokeWidth(Line edge, double newValue) {
        edge.setStrokeWidth(newValue);
    }

    @Override
    protected void selectEdge(Line edge, Paint color) {
        edge.setStroke(color);
    }

    @Override
    protected void deselectEdge(Line edge, Paint color) {
        edge.setStroke(color);
    }

    @Override
    protected void recalculateElement(Node source, Node target) {
        double sourceCenterX = getPointsCoord(source.getBoundsInLocal().getWidth(), source.getLayoutX());
        double sourceCenterY = getPointsCoord(source.getBoundsInLocal().getHeight(), source.getLayoutY());

        Line line = getEdge();

        line.startXProperty().set(sourceCenterX);
        line.startYProperty().set(sourceCenterY);

        double targetCenterX = getPointsCoord(target.getBoundsInLocal().getWidth(), target.getLayoutX());
        double targetCenterY = getPointsCoord(target.getBoundsInLocal().getHeight(), target.getLayoutY());

        //todo копипаста, исправить
        double startLeftX = source.getLayoutX();
        double startRightX = startLeftX + source.getBoundsInLocal().getWidth();
        double startTopY = source.getLayoutY();
        double startBottomY = startTopY + source.getBoundsInLocal().getHeight();
        Point2D startPoint = intersectRectangle(sourceCenterX, sourceCenterY, targetCenterX, targetCenterY, startLeftX, startRightX, startTopY, startBottomY);

        if (startPoint != null) {
            line.startXProperty().set(startPoint.x);
            line.startYProperty().set(startPoint.y);
        }

        double endLeftX = target.getLayoutX();
        double endRightX = endLeftX + target.getBoundsInLocal().getWidth();
        double endTopY = target.getLayoutY();
        double endBottomY = endTopY + target.getBoundsInLocal().getHeight();
        Point2D endPoint = intersectRectangle(sourceCenterX, sourceCenterY, targetCenterX, targetCenterY, endLeftX, endRightX, endTopY, endBottomY);

        if (endPoint != null) {
            line.endXProperty().set(endPoint.x);
            line.endYProperty().set(endPoint.y);
        }
    }

    private static double getPointsCoord(double length, double coord) {
        return length / 2 + coord;
    }

    private Point2D intersectRectangle(
            double sourceCenterX, double sourceCenterY,
            double targetCenterX, double targetCenterY,
            double leftX, double rightX,
            double topY, double bottomY) {
        return Intersector.intersectSegmentAndRectangle(
                sourceCenterX, sourceCenterY,
                targetCenterX, targetCenterY,
                leftX, rightX,
                topY, bottomY
        );
    }

//    public LineEdgeNode(String edgeId, boolean isDirectional) {
//        this(edgeId, isDirectional, null);
//    }
//
//    public LineEdgeNode(String edgeId, boolean isDirectional, Node userNode) {
//        super(
//                edgeId,
//                isDirectional,
//                new LineEdgeElement(edgeId + "_line"),
//                () -> new ArrowLineEdgeElement(edgeId + "_arrow"),
//                userNode == null? null : new UserNodeLineElement(userNode)
//        );
//    }


}
