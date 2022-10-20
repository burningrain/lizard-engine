package com.github.burningrain.gvizfx.model;

import com.github.burningrain.gvizfx.element.EdgeElement;

public class EdgeElementWrapper extends GraphElementWrapper<EdgeElement> {

    private final VertexElementWrapper source;
    private final VertexElementWrapper target;


    public EdgeElementWrapper(EdgeElement element, VertexElementWrapper source, VertexElementWrapper target) {
        super(element);
        this.source = source;
        this.target = target;
    }

    @Override
    public void accept(GraphElementWrapperVisitor visitor) {
        visitor.visit(this);
    }

    public VertexElementWrapper getSource() {
        return source;
    }

    public VertexElementWrapper getTarget() {
        return target;
    }

}
