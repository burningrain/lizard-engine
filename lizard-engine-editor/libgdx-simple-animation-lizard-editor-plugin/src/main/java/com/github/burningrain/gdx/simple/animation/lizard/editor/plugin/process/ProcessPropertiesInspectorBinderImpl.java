package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;

public class ProcessPropertiesInspectorBinderImpl implements ProcessPropertiesInspectorBinder<SimpleAnimationProcessModel, SimpleAnimationProcessInspector> {

    @Override
    public SimpleAnimationProcessModel createNewNodeModel() {
        return new SimpleAnimationProcessModel();
    }

    @Override
    public ElementDataConverter<SimpleAnimationProcessModel> getElementDataConverter() {
        return new ProcessElementDataConverterImpl();
    }

    @Override
    public void bindInspector(SimpleAnimationProcessModel model, SimpleAnimationProcessInspector inspectorNode) {
        inspectorNode.bindModel(model);
    }

    @Override
    public void unbindInspector(SimpleAnimationProcessModel model, SimpleAnimationProcessInspector inspectorNode) {
        inspectorNode.unbindModel(model);
    }

    @Override
    public SimpleAnimationProcessInspector createPropertiesInspector() {
        return new SimpleAnimationProcessInspector();
    }

}
