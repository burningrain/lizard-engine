package com.github.burningrain.lizard.editor.api;

import javafx.scene.Node;

import java.io.Serializable;

public interface ElementModelBinder<M extends Serializable, N extends Node> {

    N createNode();

    Class<N> getNodeClass();

    void bindNodeToModel(N node, M model);

    void unbindNodeToModel(N node, M model);

    M createNewNodeModel();

}
