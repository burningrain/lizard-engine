package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.utils.FxUtils;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnimationStateVertexInspector extends AnchorPane implements NodeContainer {

    private VertexInspectorController controller;


    @Override
    public void init(LizardUiApi lizardUiApi) {
        controller = FxUtils.loadFxml(new VertexInspectorController(), "/vertex_inspector.fxml");
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
