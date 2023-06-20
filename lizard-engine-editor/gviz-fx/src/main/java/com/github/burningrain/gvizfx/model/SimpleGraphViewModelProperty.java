package com.github.burningrain.gvizfx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

public class SimpleGraphViewModelProperty {

    private final SimpleMapProperty<String, VertexElementWrapper> vertices = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private final SimpleMapProperty<String, EdgeElementWrapper> edges = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private final SimpleSetProperty<GraphElementWrapper> selectedElements = new SimpleSetProperty<>(FXCollections.observableSet());

    private final BooleanProperty isClearExecuted = new SimpleBooleanProperty(false);

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

    public boolean isIsClearExecuted() {
        return isClearExecuted.get();
    }

    public BooleanProperty isClearExecutedProperty() {
        return isClearExecuted;
    }

    public void setIsClearExecuted(boolean isClearExecuted) {
        this.isClearExecuted.set(isClearExecuted);
    }

    public void clear() {
        selectedElements.clear();
        edges.clear();
        vertices.clear();

        isClearExecuted.set(true);
        isClearExecuted.set(false);
    }

}
