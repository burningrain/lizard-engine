package com.github.burningrain.lizard.editor.api;

import javafx.scene.Node;
import com.github.burningrain.lizard.engine.api.data.NodeData;

import java.io.Serializable;

public interface VertexFactory<
        M extends Serializable,
        N extends Node,
        I extends NodeContainer> extends EditorElementFactory<M, N, I, VertexModelBinder<M, N>, NodeData> {


}
