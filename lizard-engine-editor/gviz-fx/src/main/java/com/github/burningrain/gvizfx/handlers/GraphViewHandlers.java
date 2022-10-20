package com.github.burningrain.gvizfx.handlers;

public class GraphViewHandlers {

    private final GraphElementHandlers vertexHandlers = new GraphElementHandlers();
    private final GraphElementHandlers edgeHandlers = new GraphElementHandlers();

    public GraphElementHandlers getVertexHandlers() {
        return vertexHandlers;
    }

    public GraphElementHandlers getEdgeHandlers() {
        return edgeHandlers;
    }

}
