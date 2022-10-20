package com.github.burningrain.gvizfx.model;

import com.github.burningrain.gvizfx.element.GraphElement;

public abstract class GraphElementWrapper<T extends GraphElement> {

    private final T element;

    public GraphElementWrapper(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public String getElementId() {
        return element.getGraphElementId();
    }

    public void accept(GraphElementWrapperVisitor visitor) {
        visitor.visit(this);
    }

}
