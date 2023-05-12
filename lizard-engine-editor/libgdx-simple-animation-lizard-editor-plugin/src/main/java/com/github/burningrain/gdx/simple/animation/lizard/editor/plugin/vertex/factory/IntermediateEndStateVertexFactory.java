package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.factory;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.AnimationStateVertexInspector;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui.AnimationUI;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;

public class IntermediateEndStateVertexFactory extends StateVertexFactory {

    public IntermediateEndStateVertexFactory(
            ElementDataConverter<SimpleAnimationVertexModel> converter,
            VertexModelBinder<SimpleAnimationVertexModel, ? extends AnimationUI> vertexModelBinder,
            PropertiesInspectorBinder<SimpleAnimationVertexModel, AnimationStateVertexInspector> inspectorBinder) {
        super(converter, vertexModelBinder, inspectorBinder);
    }

    @Override
    public String getTitle() {
        return Constants.INTERMEDIATE_STATE;
    }

}
