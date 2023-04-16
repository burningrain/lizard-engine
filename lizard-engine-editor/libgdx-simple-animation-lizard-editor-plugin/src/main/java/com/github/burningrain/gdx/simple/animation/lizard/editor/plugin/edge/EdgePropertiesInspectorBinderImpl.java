package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge;

import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;

public class EdgePropertiesInspectorBinderImpl implements PropertiesInspectorBinder<SimpleAnimationEdgeModel, EdgePropertiesInspector> {

    @Override
    public void bindInspector(SimpleAnimationEdgeModel model, EdgePropertiesInspector inspectorNode) {
        inspectorNode.bindModel(model);
    }

    @Override
    public void unbindInspector(SimpleAnimationEdgeModel model, EdgePropertiesInspector inspectorNode) {
        inspectorNode.unbindModel(model);
    }

    @Override
    public EdgePropertiesInspector createPropertiesInspector() {
        return new EdgePropertiesInspector();
    }

}
