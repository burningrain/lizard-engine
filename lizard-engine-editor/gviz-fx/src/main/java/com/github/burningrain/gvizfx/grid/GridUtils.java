package com.github.burningrain.gvizfx.grid;

import com.github.burningrain.gvizfx.GraphViewData;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;

public class GridUtils {

    public static void relocateAndResizeGrid(GraphViewData viewData, double spacing) {
        ScrollPane scrollPane = viewData.getScrollPane();
        Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
        Bounds viewportBounds = scrollPane.getViewportBounds();

        double minX = layoutBounds.getMinX();
        double layoutWidth = layoutBounds.getWidth();
        double viewportWidth = viewportBounds.getWidth();
        double leftX = minX + (layoutWidth - viewportWidth) * scrollPane.getHvalue();

        double minY = layoutBounds.getMinY();
        double layoutHeight = layoutBounds.getHeight();
        double viewportHeight = viewportBounds.getHeight();
        double topY = minY + (layoutHeight - viewportHeight) * scrollPane.getVvalue();

        viewData.getGrid().relocateAndResize(leftX, topY, viewportWidth, viewportHeight, spacing);
    }

}
