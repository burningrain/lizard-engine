package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import com.github.burningrain.lizard.editor.api.EdgeModelBinder;

public class DefaultEdgeElementModelBinder extends DefaultElementModelBinder<DefaultGraphElementNode> implements EdgeModelBinder<DefaultGraphElementModel, DefaultGraphElementNode> {
    @Override
    public DefaultGraphElementNode createNode() {
        return new DefaultGraphElementNode();
    }

    @Override
    public Class<DefaultGraphElementNode> getNodeClass() {
        return DefaultGraphElementNode.class;
    }

}
