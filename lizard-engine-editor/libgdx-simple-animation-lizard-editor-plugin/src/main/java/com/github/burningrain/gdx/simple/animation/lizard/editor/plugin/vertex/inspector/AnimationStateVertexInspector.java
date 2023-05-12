package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.utils.FxUtils;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.VertexInspectorController;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import javafx.scene.Node;

public class AnimationStateVertexInspector implements NodeContainer {

    private VertexInspectorController controller;


    @Override
    public void init(LizardUiApi lizardUiApi) {
        controller = FxUtils.loadFxml(new VertexInspectorController(), VertexInspectorController.PATH);
    }

    @Override
    public Node getNode() {
        return controller.getPane();
    }

    public void bindModel(SimpleAnimationVertexModel model) {
        controller.bindModel(model);
    }

    public void unbindModel(SimpleAnimationVertexModel model) {
        controller.unbindModel(model);
    }

}
