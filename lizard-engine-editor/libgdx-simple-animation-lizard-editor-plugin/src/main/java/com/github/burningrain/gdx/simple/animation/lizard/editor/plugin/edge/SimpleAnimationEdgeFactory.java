package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.lizard.editor.api.*;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class SimpleAnimationEdgeFactory implements EdgeFactory<SimpleAnimationEdgeModel, Node, EdgePropertiesInspector> {

    private final EdgePropertiesInspectorBinderImpl inspectorBinder = new EdgePropertiesInspectorBinderImpl();
    private final EdgeElementDataConverterImpl dataConverter = new EdgeElementDataConverterImpl();

    @Override
    public String getTitle() {
        return Constants.ANIMATION_TRANSITION;
    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public PropertiesInspectorBinder<SimpleAnimationEdgeModel, EdgePropertiesInspector> getElementInspectorBinder() {
        return inspectorBinder;
    }

    @Override
    public EdgeModelBinder<SimpleAnimationEdgeModel, Node> getElementModelBinder() {
        return null;
    }

    @Override
    public ElementDataConverter<SimpleAnimationEdgeModel> getElementDataConverter() {
        return dataConverter;
    }

}
