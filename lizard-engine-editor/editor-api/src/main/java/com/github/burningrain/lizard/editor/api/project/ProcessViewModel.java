package com.github.burningrain.lizard.editor.api.project;

import com.github.burningrain.lizard.editor.api.project.model.*;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.io.Serializable;
import java.util.HashMap;

public interface ProcessViewModel<V extends Serializable, E extends Serializable> extends LizardModel<HashMap<String, Serializable>> {

    String getProcessName();

    String getDescription();

    ObservableMap<String, VertexViewModel<V>> getVertexes();

    ObservableMap<String, EdgeViewModel<E>> getEdges();

    ProcessElementType getSelectedEdgeType();

    SimpleObjectProperty<ProcessElementType> selectedEdgeTypeProperty();

    SimpleSetProperty<GraphElementViewModel> selectedGraphElementsProperty();

    ObservableSet<GraphElementViewModel> getSelectedGraphElements();

    Property<String> processNameProperty();

    Property<String> descriptionProperty();

}
