package com.github.burningrain.gvizfx.property;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class GridProperty {

    // This is to make the stroke be drawn 'on pixel'.
    public static final double HALF_PIXEL_OFFSET = -0.5;

    public final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(Color.GREY);
    public final SimpleDoubleProperty gridSpacing = new SimpleDoubleProperty(50);
    public final SimpleDoubleProperty strokeOffset = new SimpleDoubleProperty(HALF_PIXEL_OFFSET);
    public final SimpleBooleanProperty isSnapToGrid = new SimpleBooleanProperty(false);
    public final SimpleBooleanProperty isShowGrid = new SimpleBooleanProperty(true);

}
