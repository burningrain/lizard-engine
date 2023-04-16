package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex;

import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;

public class VertexPropertiesInspectorBinder implements PropertiesInspectorBinder<SimpleAnimationVertexModel, AnimationStateVertexInspector> {

    @Override
    public void bindInspector(SimpleAnimationVertexModel model, AnimationStateVertexInspector inspectorNode) {
        inspectorNode.bindModel(model);
    }

    @Override
    public void unbindInspector(SimpleAnimationVertexModel model, AnimationStateVertexInspector inspectorNode) {
        inspectorNode.unbindModel(model);
    }

    @Override
    public AnimationStateVertexInspector createPropertiesInspector() {
        return new AnimationStateVertexInspector();
    }

}
