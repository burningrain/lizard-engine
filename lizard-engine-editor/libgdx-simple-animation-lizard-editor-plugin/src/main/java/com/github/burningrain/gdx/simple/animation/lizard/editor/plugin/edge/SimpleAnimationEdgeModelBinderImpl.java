package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge;

import com.github.burningrain.lizard.editor.api.EdgeModelBinder;
import javafx.scene.Node;

public class SimpleAnimationEdgeModelBinderImpl implements EdgeModelBinder<SimpleAnimationEdgeModel, Node> {

    @Override
    public Node createNode() {
        return null;
    }

    @Override
    public Class<Node> getNodeClass() {
        return null;
    }

    @Override
    public void bindNodeToModel(Node node, SimpleAnimationEdgeModel model) {

    }

    @Override
    public void unbindNodeToModel(Node node, SimpleAnimationEdgeModel model) {

    }

    @Override
    public SimpleAnimationEdgeModel createNewNodeModel() {
        return new SimpleAnimationEdgeModel();
    }

}
