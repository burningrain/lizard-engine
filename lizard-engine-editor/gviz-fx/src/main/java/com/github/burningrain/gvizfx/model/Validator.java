package com.github.burningrain.gvizfx.model;

import javafx.geometry.Bounds;
import javafx.scene.Node;

public final class Validator {

    public static boolean isNeedRunLater(Node node) {
        Bounds boundsInLocal = node.getBoundsInLocal();
        return (boundsInLocal.getMinX() == 0) &&
                (boundsInLocal.getMinY() == 0) &&
                (boundsInLocal.getMinZ() == 0) &&
                (boundsInLocal.getWidth() == 0) &&
                (boundsInLocal.getHeight() == 0) &&
                (boundsInLocal.getDepth() == 0) &&
                (boundsInLocal.getMaxX() == 0) &&
                (boundsInLocal.getMaxY() == 0) &&
                (boundsInLocal.getMaxZ() == 0);
    }

}
