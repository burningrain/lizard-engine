package com.github.burningrain.gvizfx.property;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SimpleGraphElementProperty {

    public final SimpleObjectProperty<Color> defaultColor = new SimpleObjectProperty<>(Color.BLACK);
    public final SelectionProperty selectionProperty = new SelectionProperty();

    public final EdgeProperty edgeProperty = new EdgeProperty();
    public final ArrowProperty arrowProperty = new ArrowProperty();


    public static class SelectionProperty {
        public final SimpleObjectProperty<Color> selectionColor = new SimpleObjectProperty<>(Color.GREEN);
        public final SimpleObjectProperty<Border> selectionBorder = new SimpleObjectProperty<>(
                new Border(
                        new BorderStroke(selectionColor.get(), BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)
                )
        );
        public final SimpleObjectProperty<Background> selectionBackground = new SimpleObjectProperty<>(
                new Background(
                        new BackgroundFill(
                                new Color(Color.CADETBLUE.getRed(), Color.CADETBLUE.getBlue(), Color.CADETBLUE.getGreen(), 0.25),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                        )
                )
        );

        public final SimpleObjectProperty<SelectionMode> selectionMode = new SimpleObjectProperty<>(SelectionMode.ONE);

        public enum SelectionMode {
            ONE, MANY
        }

    }

}