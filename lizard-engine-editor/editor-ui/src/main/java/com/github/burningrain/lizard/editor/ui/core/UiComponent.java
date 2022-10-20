package com.github.burningrain.lizard.editor.ui.core;

import javafx.scene.Node;

public interface UiComponent<N extends Node> {

    void activate();

    void deactivate();

    N getNode();

}
