package com.github.burningrain.lizard.editor.ui.components;

import javafx.scene.Node;

import java.io.Serializable;

public interface UiController<T extends Serializable> {

    void setNode(Node node);

    Node bind(T model);

    Node unbind();

}
