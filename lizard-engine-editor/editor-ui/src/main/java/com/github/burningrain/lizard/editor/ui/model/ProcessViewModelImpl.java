package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.project.ProcessViewModel;
import com.github.burningrain.lizard.editor.api.project.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class ProcessViewModelImpl<V extends Serializable, E extends Serializable> implements ProcessViewModel  {

    private final SimpleStringProperty processName = new SimpleStringProperty();
    private final SimpleStringProperty description = new SimpleStringProperty();
    private final HashMap<String, Serializable> data = new HashMap<>();

    //todo думаю, надо это убрать и отдать на откуп пользователю. А вот о чем оповещать - о том, что узел листовой
    private final SimpleIntegerProperty startVertexId = new SimpleIntegerProperty();

    private final SimpleSetProperty<GraphElementViewModel> selectedGraphElements = new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));
    private final SimpleObjectProperty<ProcessElementType> selectedEdgeType = new SimpleObjectProperty<>();

    private ObservableMap<String, VertexViewModel<V>> vertexes = FXCollections.observableHashMap();
    private ObservableMap<String, EdgeViewModel<E>> edges = FXCollections.observableHashMap();


    public String getProcessName() {
        return processName.get();
    }

    public SimpleStringProperty processNameProperty() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName.set(processName);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public ObservableMap<String, VertexViewModel<V>> getVertexes() {
        return vertexes;
    }

    public void setVertexes(ObservableMap<String, VertexViewModel<V>> vertexes) {
        this.vertexes = vertexes;
    }

    public ObservableMap<String, EdgeViewModel<E>> getEdges() {
        return edges;
    }

    public void setEdges(ObservableMap<String, EdgeViewModel<E>> edges) {
        this.edges = edges;
    }

    public ProcessElementType getSelectedEdgeType() {
        return selectedEdgeType.get();
    }

    public SimpleObjectProperty<ProcessElementType> selectedEdgeTypeProperty() {
        return selectedEdgeType;
    }

    public void setSelectedEdgeType(ProcessElementType selectedEdgeType) {
        this.selectedEdgeType.set(selectedEdgeType);
    }

    public ObservableSet<GraphElementViewModel> getSelectedGraphElements() {
        return selectedGraphElements.get();
    }

    public SimpleSetProperty<GraphElementViewModel> selectedGraphElementsProperty() {
        return selectedGraphElements;
    }

    public void setSelectedGraphElements(ObservableSet<GraphElementViewModel> selectedGraphElements) {
        this.selectedGraphElements.set(selectedGraphElements);
    }

    public int getStartVertexId() {
        return startVertexId.get();
    }

    public SimpleIntegerProperty startVertexIdProperty() {
        return startVertexId;
    }

    public void setStartVertexId(int startVertexId) {
        this.startVertexId.set(startVertexId);
    }

    //fixme в контракте hashmap не правильно, но данные должны быть сераилизуемы. Обертку делать?
    public HashMap<String, Serializable> getData() {
        return data;
    }

    public void putDatum(String pluginId, Serializable data) {
        this.data.put(pluginId, data);
    }

}
