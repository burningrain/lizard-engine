package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.ui.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.data.ProcessData;
import com.github.burningrain.lizard.engine.api.data.TransitionData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExportImportInnerConverter {

    private final Store store;

    public ExportImportInnerConverter(Store store) {
        this.store = store;
    }

    public ProcessViewModel from(ProcessData processData) {
        ProcessViewModel processViewModel = new ProcessViewModel();
        processViewModel.setStartVertexId(processData.getStartElementId());
        processViewModel.setProcessName(processData.getTitle());
        processViewModel.setDescription(processData.getDescription());

        //FIXME здесь есть проблема. Поскольку дескриптора со списком плагинов нет, то получается
        //FIXME импорт/экспорт заточен только на работу с одним конкретным плагином...
        //FIXME сейчас список параметров без знания о плагинах передается в каждый биндер каждого плагина. Возможно пересечение названий.
        Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders = store.getProcessPropertyBinders();
        for (Map.Entry<String, ProcessPropertiesInspectorBinder> entry : processPropertyBinders.entrySet()) {
            ProcessPropertiesInspectorBinder binder = entry.getValue();
            Serializable model = binder.getElementDataConverter().importNodeData(processData.getAttributes());
            processViewModel.putDatum(entry.getKey(), model);
        }

        ObservableMap<String, VertexViewModel> vertexes = createVertexes(processData.getElements());
        processViewModel.setVertexes(vertexes);
        processViewModel.setEdges(createEdges(vertexes.values().stream().collect(Collectors.toMap(GraphElementViewModel::getId, v -> v)), processData.getTransitions()));

        return processViewModel;
    }

    private ObservableMap<String, VertexViewModel> createVertexes(List<NodeData> elements) {
        return elements.stream().map(nodeData -> {
            ProcessElementType type = ProcessElementType.of(nodeData.getPluginId(), nodeData.getTitle());

            VertexViewModel vertexViewModel = new VertexViewModel();
            vertexViewModel.setId(nodeData.getId());
            vertexViewModel.setType(type);
            vertexViewModel.setX(nodeData.getX());
            vertexViewModel.setY(nodeData.getY());

            VertexFactoryWrapper vertexFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getVertexFactories().get(type.getElementName());
            vertexViewModel.setData(vertexFactoryWrapper.getFactory().getElementDataConverter().importNodeData(nodeData.getAttributes()));

            return vertexViewModel;
        }).collect(Collectors.toMap(
                v -> v.getId() + "",
                viewModel -> viewModel,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u.getId()));
                },
                FXCollections::observableHashMap));
    }

    private ObservableMap<String, EdgeViewModel> createEdges(Map<Integer, VertexViewModel> vertexes, List<TransitionData> transitions) {
        return transitions.stream().map(transitionData -> {
            ProcessElementType type = ProcessElementType.of(transitionData.getPluginId(), transitionData.getTitle());

            EdgeViewModel edgeViewModel = new EdgeViewModel();
            edgeViewModel.setId(transitionData.getId());
            edgeViewModel.setType(type);
            edgeViewModel.setVertexSource(vertexes.get(transitionData.getSourceId()));
            edgeViewModel.setVertexTarget(vertexes.get(transitionData.getTargetId()));
            edgeViewModel.setDirectional(transitionData.isDirectional());

            EdgeFactoryWrapper edgeFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getEdgeFactories().get(type.getElementName());
            Map<String, String> attributes = transitionData.getAttributes();
            if(attributes != null && !attributes.isEmpty()) {
                edgeViewModel.setData(edgeFactoryWrapper.getFactory().getElementDataConverter().importNodeData(transitionData.getAttributes()));
            }

            return edgeViewModel;
        }).collect(Collectors.toMap(
                e -> e.getId() + "",
                edgeViewModel -> {
                    // fixme добавляем ребра здесь, ну а как еще?
                    edgeViewModel.getVertexSource().getEdges().add(edgeViewModel);
                    edgeViewModel.getVertexTarget().getEdges().add(edgeViewModel);
                    return edgeViewModel;
                },
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                FXCollections::observableHashMap));
    }

    public ProcessData to(ProcessViewModel processViewModel) {
        //todo добавить в процесс дату и айдишник честно прокидывать
        return new ProcessData(
                processViewModel.getProcessName(),              // String title,
                processViewModel.getDescription(),              // String description,
                createAttributes(processViewModel.getData()),   // Map<String, String> attributes,
                createElements(processViewModel.getVertexes()), // List<NodeData> elements,
                createTransitions(processViewModel.getEdges()), // List<TransitionData> transitions,
                processViewModel.getStartVertexId()             // int startElementId
        );
    }

    private Map<String, String> createAttributes(HashMap<String, Serializable> data) {
        HashMap<String, String> result = new HashMap<>();

        for (Map.Entry<String, Serializable> entry : data.entrySet()) {
            ProcessPropertiesInspectorBinder binder = store.getProcessPropertyBinders().get(entry.getKey());
            result.putAll(binder.getElementDataConverter().exportNodeData(entry.getValue()));
        }

        return result;
    }

    //todo тут ошибки. Надо только аттрибуты отдавать на откуп плагинам
    private List<NodeData> createElements(ObservableMap<ProcessElementType, VertexViewModel> vertexes) {
        return vertexes.values().stream().map((Function<VertexViewModel, NodeData>) (v) -> {
            ProcessElementType type = v.getType();
            VertexFactoryWrapper vertexFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getVertexFactories().get(type.getElementName());
            ElementDataConverter elementDataConverter = vertexFactoryWrapper.getFactory().getElementDataConverter();
            return new NodeData(
                    v.getId(),                                        // int id,
                    type.getPluginId(),                               // String pluginId,
                    type.getElementName(),                            // String title,
                    elementDataConverter.exportNodeData(v.getData()), // Map<String, String> attributes,
                    v.getX(),                                         // float x,
                    v.getY(),                                         // float y,
                    v.getClassName(),                                 // String className,
                    v.getDescription()                                // String description

            );
        }).collect(Collectors.toList());
    }

    //todo аналогично как и выше
    private List<TransitionData> createTransitions(ObservableMap<ProcessElementType, EdgeViewModel> edges) {
        return edges.values().stream().map((Function<EdgeViewModel, TransitionData>) (e) -> {
            ProcessElementType type = e.getType();
            EdgeFactoryWrapper edgeFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getEdgeFactories().get(type.getElementName());
            ElementDataConverter elementDataConverter = edgeFactoryWrapper.getFactory().getElementDataConverter();

            return new TransitionData(
                    e.getId(),
                    type.getPluginId(),
                    type.getElementName(),
                    e.getDescription(),
                    (elementDataConverter == null) ? null : elementDataConverter.exportNodeData(e.getData()),
                    e.getVertexSource().getId(),
                    e.getVertexTarget().getId(),
                    e.isDirectional(),
                    e.getTag()
            );
        }).collect(Collectors.toList());
    }

}
