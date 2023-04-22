package com.github.burningrain.lizard.editor.api;

import javafx.scene.Node;
import com.github.burningrain.lizard.engine.api.data.TransitionData;

import java.io.Serializable;

public interface EdgeFactory<
        M extends Serializable,
        N extends Node,
        I extends NodeContainer
        > extends EditorElementFactory<M, N, I, EdgeModelBinder<M, N>, TransitionData> {

    boolean isEdgeDirectional();

}
