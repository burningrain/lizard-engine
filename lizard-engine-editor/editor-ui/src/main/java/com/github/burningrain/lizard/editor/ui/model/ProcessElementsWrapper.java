package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.*;
import com.github.burningrain.lizard.editor.ui.api.impl.EmptyProjectLifecycleListener;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import com.github.burningrain.lizard.editor.api.ext.ProcessElementsExtPoint;

import java.util.List;
import java.util.stream.Collectors;

//TODO переписать это с мап на объект плагина, все остальное в нем. А тут просто одна мапа: название плагина - объект плагина
public class ProcessElementsWrapper {

    private final SimpleMapProperty<String, VertexFactoryWrapper> vertexFactories = new SimpleMapProperty<>();
    private final SimpleMapProperty<String, EdgeFactoryWrapper> edgeFactories = new SimpleMapProperty<>();

    private final SimpleMapProperty<String, ProcessPropertiesInspectorBinder> processInspectorBinders = new SimpleMapProperty<>();
    private final SimpleMapProperty<String, ProjectLifecycleListener> projectListeners = new SimpleMapProperty<>();

    public ProcessElementsWrapper(String pluginId, List<ProcessElementsExtPoint> extensions) {
        //TODO это надо убирать и делать один нормальный цикл
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
                                (ProcessElementsExtPoint processElementsExtPoint) -> pluginId,
                                (ProcessElementsExtPoint processElementsExtPoint) -> processElementsExtPoint.getProcessInspectorBinder()
                        ))
        ));
        projectListeners.set(FXCollections.observableMap(
                extensions.stream()
                        .collect(Collectors.toMap(
                                (ProcessElementsExtPoint processElementsExtPoint) -> pluginId,
                                (ProcessElementsExtPoint processElementsExtPoint) ->
                                        processElementsExtPoint.getProjectLifecycleListener() == null ?
                                                EmptyProjectLifecycleListener.INSTANCE :
                                                processElementsExtPoint.getProjectLifecycleListener()
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

    public ObservableMap<String, ProjectLifecycleListener> getProjectListeners() {
        return projectListeners.get();
    }

}
