package com.github.burningrain.gvizfx.element.edge.curve;

import com.github.burningrain.gvizfx.element.edge.EdgeFactory;
import javafx.scene.Node;

public class CurveEdgeFactory implements EdgeFactory<CurveEdgeElement> {

    @Override
    public CurveEdgeElement createEdge(String edgeId, boolean isDirectional, boolean selfBind, Node userNode) {
        return new CurveEdgeElement(edgeId, isDirectional, userNode);
    }

}
