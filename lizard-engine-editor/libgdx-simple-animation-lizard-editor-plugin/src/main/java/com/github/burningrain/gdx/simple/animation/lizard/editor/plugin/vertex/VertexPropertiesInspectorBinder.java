package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.AnimationStateVertexInspector;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.preview.PreviewSingleton;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;

public class VertexPropertiesInspectorBinder implements PropertiesInspectorBinder<SimpleAnimationVertexModel, AnimationStateVertexInspector> {

    @Override
    public void bindInspector(SimpleAnimationVertexModel model, AnimationStateVertexInspector inspectorNode) {
        inspectorNode.bindModel(model);
        PreviewSingleton.getInstance().bindModel(model);
    }

    @Override
    public void unbindInspector(SimpleAnimationVertexModel model, AnimationStateVertexInspector inspectorNode) {
        inspectorNode.unbindModel(model);
        PreviewSingleton.getInstance().unbindModel(model);
    }

    @Override
    public AnimationStateVertexInspector createPropertiesInspector() {
        return new AnimationStateVertexInspector();
    }

}
