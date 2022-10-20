package com.github.burningrain.gvizfx.element.edge;

import com.github.burningrain.gvizfx.property.ArrowProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Polygon;

//todo написать, что взято со stackoverflow
public class Arrow extends Polygon {

    private final ArrowProperty arrowProperty = new ArrowProperty();

    private final SimpleDoubleProperty startX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty startY = new SimpleDoubleProperty();
    private final SimpleDoubleProperty endX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty endY = new SimpleDoubleProperty();

    private final InvalidationListener updater;

    public Arrow() {
        updater = o -> {
            repaint();
        };

        // add updater to properties
        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);

        arrowProperty.length.addListener(updater);
        arrowProperty.width.addListener(updater);

        repaint();
    }

    public ArrowProperty getArrowProperty() {
        return arrowProperty;
    }

    private void repaint() {
        this.getPoints().clear();

        double endX = getEndX();
        double endY = getEndY();
        double startX = getStartX();
        double startY = getStartY();

        this.getPoints().addAll(endX, endY);
        if (endX == startX && endY == startY) {
            //todo вырожденный случай
        } else {
            double deltaX = startX - endX;
            double deltaY = startY - endY;

            double hypot = Math.hypot(deltaX, deltaY);
            double factor = arrowProperty.length.get() / hypot;
            double factorO = arrowProperty.width.get() / hypot;

            // part in direction of main line
            double dx = deltaX * factor;
            double dy = deltaY * factor;

            // part ortogonal to main line
            double ox = deltaX * factorO;
            double oy = deltaY * factorO;

            this.getPoints().addAll(endX + dx - oy, endY + dy + ox, endX + dx + oy, endY + dy - ox);
        }
    }

    // start/end properties

    public final void setStartX(double value) {
        startX.set(value);
    }

    public final double getStartX() {
        return startX.get();
    }

    public final DoubleProperty startXProperty() {
        return startX;
    }

    public final void setStartY(double value) {
        startY.set(value);
    }

    public final double getStartY() {
        return startY.get();
    }

    public final DoubleProperty startYProperty() {
        return startY;
    }

    public final void setEndX(double value) {
        endX.set(value);
    }

    public final double getEndX() {
        return endX.get();
    }

    public final DoubleProperty endXProperty() {
        return endX;
    }

    public final void setEndY(double value) {
        endY.set(value);
    }

    public final double getEndY() {
        return endY.get();
    }

    public final DoubleProperty endYProperty() {
        return endY;
    }

}