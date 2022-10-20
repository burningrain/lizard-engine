package com.github.burningrain.gvizfx.model;

import com.github.burningrain.gvizfx.element.VertexElement;

import java.util.ArrayList;
import java.util.List;

public class VertexElementWrapper extends GraphElementWrapper<VertexElement> {

    private List<EdgeElementWrapper> edgeElementList;

    public VertexElementWrapper(VertexElement element) {
        super(element);
    }

    @Override
    public void accept(GraphElementWrapperVisitor visitor) {
        visitor.visit(this);
    }

    public List<EdgeElementWrapper> getEdges() {
        if(edgeElementList == null) {
            edgeElementList = new ArrayList<>(1);
        }
        return edgeElementList;
    }

}
