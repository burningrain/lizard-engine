package com.github.burningrain.gvizfx.model;

import com.github.burningrain.gvizfx.element.EdgeElement;
import com.github.burningrain.gvizfx.element.edge.EdgeFactory;
import com.github.burningrain.gvizfx.element.VertexElement;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.Node;

public interface GraphViewModel {

    ObservableMap<String, VertexElementWrapper> getVertices();

    SimpleMapProperty<String, VertexElementWrapper> verticesProperty();

    ObservableMap<String, EdgeElementWrapper> getEdges();

    SimpleMapProperty<String, EdgeElementWrapper> edgesProperty();

    <N extends Node> VertexElement<N> addVertex(VertexElement<N> node, double x, double y);

    <N extends Node> EdgeElement addEdge(String edgeId, boolean isDirectional, String sourceId, String targetId, N userNode);

    ObservableSet<GraphElementWrapper> getSelectedElements();

    SimpleSetProperty<GraphElementWrapper> selectedElementsProperty();

    void setEdgeFactory(EdgeFactory<? extends EdgeElement> edgeFactory);

    EdgeFactory<? extends EdgeElement> getEdgeFactory();

    void clear();

}
