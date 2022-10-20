package com.github.burningrain.gvizfx.element.edge.curve;

import com.github.burningrain.gvizfx.element.EdgeElement;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;

public class CurveEdgeElement extends EdgeElement<Path, ArrowCurveEdgeElement> {

    private static Path createPath() {
        Path path = new Path();
        path.getElements().add(new MoveTo());
        path.getElements().add(new QuadCurveTo());

        return path;
    }

    public CurveEdgeElement(String graphElementId, boolean isDirectional) {
        this(graphElementId, isDirectional, null);
    }

    public CurveEdgeElement(String graphElementId, boolean isDirectional, Node node) {
        super(
                graphElementId,
                isDirectional,
                createPath(),
                () -> new ArrowCurveEdgeElement(),
                null
        );
    }

    @Override
    protected void setColor(Path edge, Paint color) {
        edge.fillProperty().set(color);
    }

    @Override
    protected void setStrokeWidth(Path edge, double newValue) {
        edge.setStrokeWidth(newValue);
    }

    @Override
    protected void selectEdge(Path edge, Paint color) {
        edge.fillProperty().set(color);
    }

    @Override
    protected void deselectEdge(Path edge, Paint color) {
        edge.fillProperty().set(color);
    }

    @Override
    protected void recalculateElement(Node source, Node target) {
        Path path = getEdge();
        MoveTo moveTo = (MoveTo) path.getElements().get(0);
        QuadCurveTo quadCurveTo = (QuadCurveTo) path.getElements().get(1);

        moveTo.xProperty().bind(source.layoutXProperty());
        moveTo.yProperty().bind(source.layoutYProperty());

        quadCurveTo.xProperty().bind(target.layoutXProperty());
        quadCurveTo.yProperty().bind(target.layoutYProperty());

        source.layoutXProperty().addListener((observable, oldValue, newValue) -> {
            int diff = 100;
            if (target.getLayoutX() - newValue.doubleValue() < 0) {
                diff = -diff;
            }
            quadCurveTo.controlXProperty().set(newValue.doubleValue() + diff);
        });
        source.layoutYProperty().addListener((observable, oldValue, newValue) -> {
            int diff = 100;
            if (target.getLayoutY() - newValue.doubleValue() < 0) {
                diff = -diff;
            }
            quadCurveTo.controlYProperty().set(newValue.doubleValue() + diff);
        });
        target.layoutXProperty().addListener((observable, oldValue, newValue) -> {
            int diff = 100;
            if (newValue.doubleValue() - source.getLayoutX() < 0) {
                diff = -diff;
            }
            quadCurveTo.controlXProperty().set(newValue.doubleValue() - diff);
        });
        target.layoutYProperty().addListener((observable, oldValue, newValue) -> {
            int diff = 100;
            if (newValue.doubleValue() - source.getLayoutY() < 0) {
                diff = -diff;
            }
            quadCurveTo.controlYProperty().set(newValue.doubleValue() - diff);
        });
    }

}
