package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import javafx.scene.Node;

public interface AnimationStateVertexInspector extends NodeContainer {
    @Override
    void init(LizardUiApi lizardUiApi);

    @Override
    Node getNode();

    void bindModel(SimpleAnimationVertexModel model);

    void unbindModel(SimpleAnimationVertexModel model);
}
