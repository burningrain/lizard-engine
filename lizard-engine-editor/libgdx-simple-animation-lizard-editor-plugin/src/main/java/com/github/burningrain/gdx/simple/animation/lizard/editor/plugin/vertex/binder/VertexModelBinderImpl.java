package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.binder;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui.AnimationUI;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;

import java.util.function.Supplier;

public class VertexModelBinderImpl<T extends AnimationUI> implements VertexModelBinder<SimpleAnimationVertexModel, T> {

    private final Class<T> nodeClass;
    private final Supplier<T> supplier;

    public VertexModelBinderImpl(Class<T> nodeClass, Supplier<T> supplier) {
        this.nodeClass = nodeClass;
        this.supplier = supplier;
    }

    @Override
    public T createNode() {
        return supplier.get();
    }

    @Override
    public Class<T> getNodeClass() {
        return nodeClass;
    }

    @Override
    public void bindNodeToModel(AnimationUI node, SimpleAnimationVertexModel model) {
        node.bindModel(model);
    }

    @Override
    public void unbindNodeToModel(AnimationUI node, SimpleAnimationVertexModel model) {
        node.unbindModel(model);
    }

    @Override
    public SimpleAnimationVertexModel createNewNodeModel() {
        return new SimpleAnimationVertexModel();
    }

}
