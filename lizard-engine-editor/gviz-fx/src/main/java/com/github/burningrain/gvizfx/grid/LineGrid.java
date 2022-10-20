package com.github.burningrain.gvizfx.grid;

import com.github.burningrain.gvizfx.property.GridProperty;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class LineGrid extends AbstractBackgroundGrid {

    private final Path mGrid = new Path();

    @Override
    public void init(GridProperty gridProperty) {
        super.init(gridProperty);

        getChildren().add(mGrid);
        mGrid.strokeProperty().bind(gridProperty.color);
        mGrid.relocate(1.0, 1.0); //fixme пофиксать, когда станет понятно откуда же идет сдвиг на 1.5 пикселя (если убрать)
    }

    @Override
    protected void drawGrid(double x0, double y0, double pWidth, double pHeight, double spacing, double deltaX, double deltaY) {
        mGrid.getElements().clear();

        final int hLineCount = (int) Math.floor((pHeight + 1) / spacing) + 1;
        final int vLineCount = (int) Math.floor((pWidth + 1) / spacing) + 1;

        double strokeOffset = gridProperty.strokeOffset.get();
        for (int i = 0; i < hLineCount; i++) {
            final double y = (i + 1) * spacing + strokeOffset - deltaY;
            mGrid.getElements().add(new MoveTo(0, y));
            mGrid.getElements().add(new LineTo(pWidth, y));
        }

        for (int i = 0; i < vLineCount; i++) {
            final double x = (i + 1) * spacing + strokeOffset - deltaX;
            mGrid.getElements().add(new MoveTo(x, 0));
            mGrid.getElements().add(new LineTo(x, pHeight));
        }
    }

}
