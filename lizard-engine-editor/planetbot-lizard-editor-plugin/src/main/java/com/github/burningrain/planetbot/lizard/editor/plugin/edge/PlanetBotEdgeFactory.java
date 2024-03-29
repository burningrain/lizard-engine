package com.github.burningrain.planetbot.lizard.editor.plugin.edge;

import com.github.burningrain.lizard.editor.api.*;
import com.github.burningrain.planetbot.lizard.editor.plugin.io.Constants;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class PlanetBotEdgeFactory implements EdgeFactory<PlanetBotEdgeModel, Node, NodeContainer> {

    @Override
    public String getTitle() {
        return Constants.EDGE_TITLE;
    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public PropertiesInspectorBinder<PlanetBotEdgeModel, NodeContainer> getElementInspectorBinder() {
        return null;
    }

    @Override
    public EdgeModelBinder<PlanetBotEdgeModel, Node> getElementModelBinder() {
        return null;
    }

    @Override
    public ElementDataConverter<PlanetBotEdgeModel> getElementDataConverter() {
        return null;
    }

    @Override
    public boolean isEdgeDirectional() {
        return false;
    }

}
