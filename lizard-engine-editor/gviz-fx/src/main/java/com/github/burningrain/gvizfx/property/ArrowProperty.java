package com.github.burningrain.gvizfx.property;

import javafx.beans.property.SimpleDoubleProperty;

public class ArrowProperty {

    public final SimpleDoubleProperty length = new SimpleDoubleProperty(20);
    public final SimpleDoubleProperty width = new SimpleDoubleProperty(4);

    public void bind(ArrowProperty arrowProperty) {
        length.bind(arrowProperty.length);
        width.bind(arrowProperty.width);
    }

    public void unbind() {
        length.unbind();
        width.unbind();
    }

}
