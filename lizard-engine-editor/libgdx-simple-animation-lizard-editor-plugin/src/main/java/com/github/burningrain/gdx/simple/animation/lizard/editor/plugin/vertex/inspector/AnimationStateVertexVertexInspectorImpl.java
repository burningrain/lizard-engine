package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.utils.FxUtils;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.VertexInspectorController;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import javafx.scene.Node;

public class AnimationStateVertexVertexInspectorImpl implements AnimationStateVertexInspector {

    private VertexInspectorController controller;


    @Override
    public void init(LizardUiApi lizardUiApi) {
        controller = FxUtils.loadFxml(new VertexInspectorController(), VertexInspectorController.PATH);
    }

    @Override
    public Node getNode() {
        return controller.getPane();
    }

    @Override
    public void bindModel(SimpleAnimationVertexModel model) {
        controller.bindModel(model);
    }

    @Override
    public void unbindModel(SimpleAnimationVertexModel model) {
        controller.unbindModel(model);
    }

}
