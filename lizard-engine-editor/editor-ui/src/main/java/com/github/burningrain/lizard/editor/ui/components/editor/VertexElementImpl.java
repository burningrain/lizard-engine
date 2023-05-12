package com.github.burningrain.lizard.editor.ui.components.editor;

import com.github.burningrain.gvizfx.element.VertexElement;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;
import com.github.burningrain.lizard.editor.api.project.model.VertexViewModel;
import javafx.scene.Node;

import java.io.Serializable;

public class VertexElementImpl<D extends Serializable, N extends Node> extends VertexElement<N> {

    private final VertexViewModel<D> vertexViewModel;
    private final VertexModelBinder<D, N> modelBinder;

    public VertexElementImpl(VertexViewModel<D> vertexViewModel, VertexModelBinder<D, N> modelBinder, N node) {
        super(vertexViewModel.getId() + "", node);
        this.vertexViewModel = vertexViewModel;
        this.modelBinder = modelBinder;
    }

    @Override
    public N copyNodeForMinimap() {
        N node = modelBinder.createNode();
        modelBinder.bindNodeToModel(node, vertexViewModel.getData());
        return node;
    }

}
