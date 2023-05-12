package com.github.burningrain.lizard.editor.ui.components.controllers;

import com.github.burningrain.lizard.editor.api.project.model.LizardModel;
import javafx.scene.Node;
import com.github.burningrain.lizard.editor.ui.components.UiController;

public abstract class DefaultInspectorController<T extends LizardModel> implements UiController<T> {

    private Node node;
    private T currentElementViewModel;

    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public Node bind(T currentElementViewModel) {
        this.currentElementViewModel = currentElementViewModel;
        bindData(currentElementViewModel);
        return node;
    }

    @Override
    public Node unbind() {
        unbindData(currentElementViewModel);
        return node;
    }

    public Node getNode() {
        return node;
    }

    protected abstract void bindData(T currentElementViewModel);
    protected abstract void unbindData(T currentElementViewModel);

}
