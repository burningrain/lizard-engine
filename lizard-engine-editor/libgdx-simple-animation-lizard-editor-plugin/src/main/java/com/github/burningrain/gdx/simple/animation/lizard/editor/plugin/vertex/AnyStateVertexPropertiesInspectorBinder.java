package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.AnimationStateVertexInspector;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.AnyStateAnimationStateVertexInspectorImpl;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;

public class AnyStateVertexPropertiesInspectorBinder implements PropertiesInspectorBinder<SimpleAnimationVertexModel, AnimationStateVertexInspector> {
    @Override
    public void bindInspector(SimpleAnimationVertexModel model, AnimationStateVertexInspector inspectorNode) {

    }

    @Override
    public void unbindInspector(SimpleAnimationVertexModel model, AnimationStateVertexInspector inspectorNode) {

    }

    @Override
    public AnimationStateVertexInspector createPropertiesInspector() {
        return new AnyStateAnimationStateVertexInspectorImpl();
    }
}
