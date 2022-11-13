package com.github.burningrain.gvizfx.overview;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;

public interface GraphOverview {

    void bind(GraphViewData graphViewData, GraphViewProperty graphViewProperty);

    void unbind();

    void update();

    Node getNode();

    DoubleProperty fitWidthProperty();

    DoubleProperty fitHeightProperty();

    GraphOverviewData getData();

    Image snapshot();

}
