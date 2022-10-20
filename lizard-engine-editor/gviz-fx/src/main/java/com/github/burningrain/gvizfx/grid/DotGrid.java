package com.github.burningrain.gvizfx.grid;


import com.github.burningrain.gvizfx.property.GridProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class DotGrid extends AbstractBackgroundGrid {

    private final ObservableList<Circle> circles = FXCollections.observableList(new ArrayList<>());

    @Override
    public void init(GridProperty gridProperty) {
        super.init(gridProperty);
    }

    @Override
    protected void drawGrid(double x0, double y0, double pWidth, double pHeight, double spacing, double deltaX, double deltaY) {
        getChildren().removeAll(circles);
        circles.clear();

        final int hLineCount = (int) Math.floor((pHeight + 1) / spacing);
        final int vLineCount = (int) Math.floor((pWidth + 1) / spacing);

        for (int i = 0; i < hLineCount; i++) {
            for (int j = 0; j < vLineCount; j++) {
                final double y = (i + 1) * spacing - deltaY;
                final double x = (j + 1) * spacing - deltaX;
                circles.add(new Circle(x, y, 1, gridProperty.color.get()));
            }
        }

        getChildren().addAll(circles);
    }

}
