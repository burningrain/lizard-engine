package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AnyStateAnimationStateVertexInspectorImpl implements AnimationStateVertexInspector {

    private Pane pane = null;

    @Override
    public void init(LizardUiApi lizardUiApi) {
        pane = new Pane();
    }

    @Override
    public Node getNode() {
        return pane;
    }

    @Override
    public void bindModel(SimpleAnimationVertexModel model) {

    }

    @Override
    public void unbindModel(SimpleAnimationVertexModel model) {

    }

}
