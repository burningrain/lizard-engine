package com.github.burningrain.gvizfx.element.edge.line;

import com.github.burningrain.gvizfx.element.EdgeElement;
import com.github.burningrain.gvizfx.element.edge.EdgeFactory;
import javafx.scene.Node;

import java.util.Objects;

public class LineEdgeFactory<T extends EdgeElement> implements EdgeFactory<T> {

    @Override
    public T createEdge(String edgeId, boolean isDirectional, boolean selfBind, Node userNode) {
        Objects.requireNonNull(edgeId);
        if(selfBind) {
            return (T) new PolylineSelfEdgeElement(edgeId, isDirectional, userNode);
        }
        return (T) new LineEdgeElement(edgeId, isDirectional, userNode);
    }

}
