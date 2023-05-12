package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.EditorElementFactory;
import com.github.burningrain.lizard.editor.api.project.model.ProcessElementType;

public abstract class ElementFactoryWrapper<F extends EditorElementFactory> {

    private final ProcessElementType type;
    private final F factory;

    public ElementFactoryWrapper(ProcessElementType type, F factory) {
        this.type = type;
        this.factory = factory;
    }

    public ProcessElementType getType() {
        return type;
    }

    public F getFactory() {
        return factory;
    }

}
