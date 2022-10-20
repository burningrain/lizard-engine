package com.github.burningrain.lizard.editor.api;

import javafx.scene.Node;

import java.io.Serializable;

public interface EdgeModelBinder<M extends Serializable, N extends Node> extends ElementModelBinder<M, N> {

    //void bindEdgeNode(N edge, Node sourceVertex, Node targetVertex);

}
