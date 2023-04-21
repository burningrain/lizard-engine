package com.github.burningrain.gvizfx.element.edge.line;

import com.github.burningrain.gvizfx.Intersector;
import com.github.burningrain.gvizfx.element.EdgeElement;
import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;

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
    protected void recalculateElement(Node source, Node target, Scale scale) {
        double sourceCenterX = getPointsCoord(source.getBoundsInLocal().getWidth() * scale.getX(), source.getLayoutX());
        double sourceCenterY = getPointsCoord(source.getBoundsInLocal().getHeight() * scale.getY(), source.getLayoutY());

        Line line = getEdge();

        line.startXProperty().set(sourceCenterX);
        line.startYProperty().set(sourceCenterY);

        double targetCenterX = getPointsCoord(target.getBoundsInLocal().getWidth() * scale.getX(), target.getLayoutX());
        double targetCenterY = getPointsCoord(target.getBoundsInLocal().getHeight() * scale.getY(), target.getLayoutY());

        //todo копипаста, исправить
        double startLeftX = source.getLayoutX();
        double startRightX = startLeftX + source.getBoundsInLocal().getWidth() * scale.getX();
        double startTopY = source.getLayoutY();
        double startBottomY = startTopY + source.getBoundsInLocal().getHeight() * scale.getY();
        Point2D startPoint = intersectRectangle(sourceCenterX, sourceCenterY, targetCenterX, targetCenterY, startLeftX, startRightX, startTopY, startBottomY);

        if (startPoint != null) {
            line.startXProperty().set(startPoint.x);
            line.startYProperty().set(startPoint.y);
        }

        double endLeftX = target.getLayoutX();
        double endRightX = endLeftX + target.getBoundsInLocal().getWidth() * scale.getX();
        double endTopY = target.getLayoutY();
        double endBottomY = endTopY + target.getBoundsInLocal().getHeight() * scale.getY();
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


}
