package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.ext.ProcessElementsExtPoint;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessElementsWrapper {

    private SimpleMapProperty<String, VertexFactoryWrapper> vertexFactories = new SimpleMapProperty<>();
    private SimpleMapProperty<String, EdgeFactoryWrapper> edgeFactories = new SimpleMapProperty<>();

    private SimpleMapProperty<String, ProcessPropertiesInspectorBinder> processInspectorBinders = new SimpleMapProperty<>();

    public ProcessElementsWrapper(String pluginId, List<ProcessElementsExtPoint> extensions) {
        vertexFactories.set(FXCollections.observableMap(
                extensions.stream()
                        .flatMap(processElementsExtPoint -> processElementsExtPoint.getVertexFactoriesList().stream())
                        .collect(Collectors.toMap(VertexFactory::getTitle, factory -> VertexFactoryWrapper.of(pluginId, factory)))));
        edgeFactories.set(FXCollections.observableMap(
                extensions.stream()
                        .flatMap(processElementsExtPoint -> processElementsExtPoint.getEdgeFactoriesList().stream())
                        .collect(Collectors.toMap(EdgeFactory::getTitle, factory -> EdgeFactoryWrapper.of(pluginId, factory)))));
        processInspectorBinders.set(FXCollections.observableMap(
                extensions.stream()
                        .collect(Collectors.toMap(
                                (ProcessElementsExtPoint processElementsExtPoint)-> pluginId,
                                (ProcessElementsExtPoint processElementsExtPoint)-> processElementsExtPoint.getProcessInspectorBinder()
                        ))
        ));
    }

    public ObservableMap<String, VertexFactoryWrapper> getVertexFactories() {
        return vertexFactories.get();
    }

    public ObservableMap<String, EdgeFactoryWrapper> getEdgeFactories() {
        return edgeFactories.get();
    }

    public ObservableMap<String, ProcessPropertiesInspectorBinder> getProcessInspectorBinders() {
        return processInspectorBinders.get();
    }

}
