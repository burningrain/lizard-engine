package com.github.burningrain.gvizfx.model;

public interface GraphElementWrapperVisitor {

    void visit(VertexElementWrapper vertexElementWrapper);

    void visit(EdgeElementWrapper edgeElementWrapper);

    void visit(GraphElementWrapper graphElementWrapper);

}
