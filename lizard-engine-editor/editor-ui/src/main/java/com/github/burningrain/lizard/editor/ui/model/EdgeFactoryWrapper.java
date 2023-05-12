package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.api.project.model.ProcessElementType;
import com.github.burningrain.lizard.editor.ui.model.defaultmodel.DefaultEdgeFactory;

public class EdgeFactoryWrapper extends ElementFactoryWrapper<EdgeFactory> {

    private EdgeFactoryWrapper(ProcessElementType type, EdgeFactory factory) {
        super(type, factory);
    }

    public static EdgeFactoryWrapper of(String pluginId, EdgeFactory factory) {
        return new EdgeFactoryWrapper(ProcessElementType.of(pluginId, factory.getTitle()), factory);
    }

    public static EdgeFactoryWrapper createDefault(ProcessElementType elementType) {
        return new EdgeFactoryWrapper(elementType, new DefaultEdgeFactory());
    }

}
