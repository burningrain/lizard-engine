package com.github.burningrain.gvizfx.element.edge;

import com.github.burningrain.gvizfx.element.EdgeElement;
import javafx.scene.Node;

public interface EdgeFactory<E extends EdgeElement> {

    E createEdge(String edgeId, boolean isDirectional, boolean selfBind, Node userNode);

}