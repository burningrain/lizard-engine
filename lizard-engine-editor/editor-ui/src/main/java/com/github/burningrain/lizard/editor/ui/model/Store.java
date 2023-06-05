package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;
import com.github.burningrain.lizard.editor.api.project.model.ProcessElementType;
import com.github.burningrain.lizard.editor.api.project.model.descriptor.PluginDescriptor;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;

import java.util.*;
import java.util.stream.Collectors;

public class Store {

    private final SimpleObjectProperty<ProjectModel> currentProjectModel = new SimpleObjectProperty<>();

    private final SimpleMapProperty<String, ProcessElementsWrapper> processElements = new SimpleMapProperty<>();
    private final SimpleMapProperty<String, IOWrapper> ioPoints = new SimpleMapProperty<>();

    public ObservableMap<String, ProcessElementsWrapper> getProcessElements() {
        return processElements.get();
    }

    public SimpleMapProperty<String, ProcessElementsWrapper> processElementsProperty() {
        return processElements;
    }

    public void setProcessElements(ObservableMap<String, ProcessElementsWrapper> processElements) {
        this.processElements.set(processElements);
    }

    public ProjectModel getCurrentProjectModel() {
        return currentProjectModel.get();
    }

    public Set<String> getCurrentProjectPluginIds() {
        return currentProjectModel.get().getDescriptor().getPluginDescriptors()
                .stream()
                .map(PluginDescriptor::getPluginId)
                .collect(Collectors.toSet());
    }

    public SimpleObjectProperty<ProjectModel> currentProjectModelProperty() {
        return currentProjectModel;
    }

    public void setCurrentProjectModel(ProjectModel currentProcessViewModel) {
        this.currentProjectModel.set(currentProcessViewModel);
    }

    public ObservableMap<String, IOWrapper> getIoPoints() {
        return ioPoints.get();
    }

    public Collection<String> getPluginsExtensionsTitles() {
        return ioPoints.keySet();
    }

    public Collection<String> getAllPluginsTitles() {
        return processElements.keySet();
    }

    public SimpleMapProperty<String, IOWrapper> ioPointsProperty() {
        return ioPoints;
    }

    public void setIoPoints(ObservableMap<String, IOWrapper> ioPoints) {
        this.ioPoints.set(ioPoints);
    }

    public VertexFactoryWrapper getVertexFactoryWrapper(ProcessElementType type) {
        ProcessElementsWrapper processElementsWrapper = getProcessElementsWrapper(type);
        if(processElementsWrapper != null) {
            ObservableMap<String, VertexFactoryWrapper> vertexFactories = processElementsWrapper.getVertexFactories();
            if(!vertexFactories.isEmpty()) {
                return vertexFactories.get(type.getElementName());
            }
        }
        return null;
    }

    public EdgeFactoryWrapper getEdgeFactoryWrapper(ProcessElementType type) {
        ProcessElementsWrapper processElementsWrapper = getProcessElementsWrapper(type);
        if(processElementsWrapper != null) {
            ObservableMap<String, EdgeFactoryWrapper> edgeFactories = processElementsWrapper.getEdgeFactories();
            if(!edgeFactories.isEmpty()) {
                return edgeFactories.get(type.getElementName());
            }
        }
        return null;
    }

    public Map<String, ProcessPropertiesInspectorBinder> getProcessPropertyBinders() {
        HashMap<String, ProcessPropertiesInspectorBinder> result = new HashMap<>();
        for (ProcessElementsWrapper wrapper : processElements.values()) {
            result.putAll(wrapper.getProcessInspectorBinders());
        }

        return result;
    }

    private ProcessElementsWrapper getProcessElementsWrapper(ProcessElementType type) {
        Objects.requireNonNull(type);
        return  !(processElements.isEmpty())? processElements.get(type.getPluginId()) : null;
    }

}
