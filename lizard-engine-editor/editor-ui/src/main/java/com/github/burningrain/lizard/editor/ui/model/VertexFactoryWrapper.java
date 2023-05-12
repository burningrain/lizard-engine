package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.project.model.ProcessElementType;
import com.github.burningrain.lizard.editor.ui.model.defaultmodel.DefaultVertexFactory;

public class VertexFactoryWrapper extends ElementFactoryWrapper<VertexFactory> {

    private VertexFactoryWrapper(ProcessElementType type, VertexFactory factory) {
        super(type, factory);
    }

    public static VertexFactoryWrapper of(String pluginId, VertexFactory factory) {
        return new VertexFactoryWrapper(ProcessElementType.of(pluginId, factory.getTitle()), factory);
    }

    public static VertexFactoryWrapper createDefault(ProcessElementType elementType) {
        return new VertexFactoryWrapper(elementType, new DefaultVertexFactory());
    }

}
