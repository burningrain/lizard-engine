package com.github.burningrain.lizard.editor.api;

import javafx.scene.Node;

public interface NodeContainer {

    void init(LizardUiApi lizardUiApi);

    Node getNode();

}
