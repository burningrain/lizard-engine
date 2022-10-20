package com.github.burningrain.gvizfx.model;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

public class SimpleGraphViewModelProperty {

    private final SimpleMapProperty<String, VertexElementWrapper> vertices = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private final SimpleMapProperty<String, EdgeElementWrapper> edges = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private final SimpleSetProperty<GraphElementWrapper> selectedElements = new SimpleSetProperty<>(FXCollections.observableSet());

    public ObservableMap<String, VertexElementWrapper> getVertices() {
        return vertices.get();
    }

    public SimpleMapProperty<String, VertexElementWrapper> verticesProperty() {
        return vertices;
    }

    public void setVertices(ObservableMap<String, VertexElementWrapper> vertices) {
        this.vertices.set(vertices);
    }

    public ObservableMap<String, EdgeElementWrapper> getEdges() {
        return edges.get();
    }

    public SimpleMapProperty<String, EdgeElementWrapper> edgesProperty() {
        return edges;
    }

    public void setEdges(ObservableMap<String, EdgeElementWrapper> edges) {
        this.edges.set(edges);
    }

    public ObservableSet<GraphElementWrapper> getSelectedElements() {
        return selectedElements.get();
    }

    public SimpleSetProperty<GraphElementWrapper> selectedElementsProperty() {
        return selectedElements;
    }

    public void setSelectedElements(ObservableSet<GraphElementWrapper> selectedElements) {
        this.selectedElements.set(selectedElements);
    }

    public void clear() {
        vertices.clear();
        edges.clear();
        selectedElements.clear();
    }

}
