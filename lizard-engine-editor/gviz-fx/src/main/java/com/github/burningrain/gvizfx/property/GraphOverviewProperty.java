package com.github.burningrain.gvizfx.property;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GraphOverviewProperty {

    public final SimpleObjectProperty<Color> rectangleColor = new SimpleObjectProperty<>(Color.CADETBLUE);
    public final SimpleObjectProperty<Border> rectangleBorder = new SimpleObjectProperty<>(
            new Border(
                    new BorderStroke(rectangleColor.get(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
    );

    public final SimpleDoubleProperty minVertexWidth = new SimpleDoubleProperty(4);
    public final SimpleDoubleProperty minVertexHeight = new SimpleDoubleProperty(4);

    public final SimpleDoubleProperty minEdgeStrokeWidth = new SimpleDoubleProperty(0.2);
    public final SimpleDoubleProperty maxEdgeStrokeWidth = new SimpleDoubleProperty(5.0);

}
