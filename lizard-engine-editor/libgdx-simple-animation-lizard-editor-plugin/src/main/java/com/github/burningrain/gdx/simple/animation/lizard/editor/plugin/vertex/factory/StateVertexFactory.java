package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.factory;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.*;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.AnimationStateVertexInspector;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.AnimationStateVertexVertexInspectorImpl;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui.AnimationUI;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;
import javafx.scene.image.ImageView;


public abstract class StateVertexFactory implements VertexFactory<SimpleAnimationVertexModel, AnimationUI, AnimationStateVertexInspector> {

    private final ElementDataConverter<SimpleAnimationVertexModel> converter;
    private final VertexModelBinder<SimpleAnimationVertexModel, ? extends AnimationUI> vertexModelBinder;
    private final PropertiesInspectorBinder<SimpleAnimationVertexModel, AnimationStateVertexInspector> inspectorBinder;

    public StateVertexFactory(ElementDataConverter<SimpleAnimationVertexModel> converter,
                              VertexModelBinder<SimpleAnimationVertexModel, ? extends AnimationUI> vertexModelBinder,
                              PropertiesInspectorBinder<SimpleAnimationVertexModel, AnimationStateVertexInspector> inspectorBinder) {
        this.converter = converter;
        this.vertexModelBinder = vertexModelBinder;
        this.inspectorBinder = inspectorBinder;
    }

    public abstract String getTitle();

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public PropertiesInspectorBinder<SimpleAnimationVertexModel, AnimationStateVertexInspector> getElementInspectorBinder() {
        return inspectorBinder;
    }

    @Override
    public VertexModelBinder<SimpleAnimationVertexModel, ? extends AnimationUI> getElementModelBinder() {
        return vertexModelBinder;
    }

    @Override
    public ElementDataConverter<SimpleAnimationVertexModel> getElementDataConverter() {
        return converter;
    }

}
