package com.github.burningrain.gvizfx.overview;

import com.github.burningrain.gvizfx.ViewData;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class GraphOverviewData implements ViewData {

    private final GraphOverview graphOverview;
    private final Region rectangle;

    private final Supplier<Double> width;
    private final Supplier<Double> height;

    public GraphOverviewData(GraphOverview graphOverview, Region rectangle, Supplier<Double> width, Supplier<Double> height) {
        this.graphOverview = graphOverview;
        this.rectangle = rectangle;
        this.width = width;
        this.height = height;
    }

    public GraphOverview getGraphOverview() {
        return graphOverview;
    }

    public Region getRectangle() {
        return rectangle;
    }

    public double getWidth() {
        return width.get();
    }

    public double getHeight() {
        return height.get();
    }

}
