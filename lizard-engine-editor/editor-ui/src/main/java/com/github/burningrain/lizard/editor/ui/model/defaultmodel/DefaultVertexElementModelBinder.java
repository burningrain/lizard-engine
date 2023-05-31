package com.github.burningrain.lizard.editor.ui.model.defaultmodel;


import com.github.burningrain.lizard.editor.api.VertexModelBinder;
import javafx.scene.shape.Circle;

public class DefaultVertexElementModelBinder extends DefaultElementModelBinder<DefaultGraphElementNode> implements VertexModelBinder<DefaultGraphElementModel, DefaultGraphElementNode> {
    @Override
    public DefaultGraphElementNode createNode() {
        DefaultGraphElementNode node = new DefaultGraphElementNode();
        Circle circle = new Circle(10, 10, 10);
        node.getChildren().add(circle);

        return node;
    }

    @Override
    public Class<DefaultGraphElementNode> getNodeClass() {
        return DefaultGraphElementNode.class;
    }

}
