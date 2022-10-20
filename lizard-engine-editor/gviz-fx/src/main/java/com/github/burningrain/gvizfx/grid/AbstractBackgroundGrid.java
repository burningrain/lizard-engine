package com.github.burningrain.gvizfx.grid;

import com.github.burningrain.gvizfx.property.GridProperty;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public abstract class AbstractBackgroundGrid extends Region implements BackgroundGrid {

    protected GridProperty gridProperty;

    public AbstractBackgroundGrid() {
        setManaged(false);
        setMouseTransparent(true);
    }

    @Override
    public void init(GridProperty gridProperty) {
        this.gridProperty = gridProperty;
    }

    public void relocateAndResize(double x0, double y0, double pWidth, double pHeight, double spacing) {
        super.resize(pWidth, pHeight);

        draw(x0, y0, pWidth, pHeight, spacing);
        super.relocate(x0,y0);
    }

    /**
     * Draws the grid for the given width and height.
     *
     * @param pWidth  the width of the editor region
     * @param pHeight the height of the editor region
     */
    private void draw(double x0, double y0, double pWidth, double pHeight, double spacing) {
        double deltaX = (x0 > 0)? Math.abs(x0) % spacing : spacing - Math.abs(x0) % spacing;
        double deltaY = (y0 > 0)? Math.abs(y0) % spacing : spacing - Math.abs(y0) % spacing;

        drawGrid(x0, y0, pWidth, pHeight, spacing, deltaX, deltaY);
    }

    protected abstract void drawGrid(double x0, double y0, double pWidth, double pHeight, double spacing, double deltaX, double deltaY);

    @Override
    public Node getNode() {
        return this;
    }

}
