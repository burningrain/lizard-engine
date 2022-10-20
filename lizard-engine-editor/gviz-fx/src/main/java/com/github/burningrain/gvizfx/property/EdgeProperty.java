package com.github.burningrain.gvizfx.property;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class EdgeProperty {

    public final SimpleDoubleProperty strokeWidth = new SimpleDoubleProperty(1.0);
    public final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(Color.BLACK);


    public void bind(EdgeProperty edgeProperty) {
        strokeWidth.bind(edgeProperty.strokeWidth);
        color.bind(edgeProperty.color);
    }

    public void unbind() {
        strokeWidth.unbind();
        color.unbind();
    }

}