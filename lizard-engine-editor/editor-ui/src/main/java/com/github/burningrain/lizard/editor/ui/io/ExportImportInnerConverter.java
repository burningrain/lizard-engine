package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.ui.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.data.ProcessData;
import com.github.burningrain.lizard.engine.api.data.TransitionData;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExportImportInnerConverter {

    private Store store;

    public ExportImportInnerConverter(Store store) {
        this.store = store;
    }

    public ProcessViewModel from(ProcessData processData) {
        ProcessViewModel processViewModel = new ProcessViewModel();
        processViewModel.setStartVertexId(processData.getStartElementId());
        processViewModel.setProcessName(processData.getTitle());
        processViewModel.setDescription(processData.getDescription());

        ObservableMap<ProcessElementType, VertexViewModel> vertexes = createVertexes(processData.getElements());
        processViewModel.setVertexes(vertexes);
        processViewModel.setEdges(createEdges(vertexes.values().stream().collect(Collectors.toMap(GraphElementViewModel::getId, v -> v)), processData.getTransitions()));

        return processViewModel;
    }

    private ObservableMap<ProcessElementType, VertexViewModel> createVertexes(List<NodeData> elements) {
        return elements.stream().map(nodeData -> {
            ProcessElementType type = ProcessElementType.of(nodeData.getPluginId(), nodeData.getTitle());

            VertexViewModel vertexViewModel = new VertexViewModel();
            vertexViewModel.setId(nodeData.getId());
            vertexViewModel.setType(type);

            VertexFactoryWrapper vertexFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getVertexFactories().get(type.getElementName());
            vertexViewModel.setData(vertexFactoryWrapper.getFactory().getElementDataConverter().importNodeData(nodeData.getAttributes()));

            return vertexViewModel;
        }).collect(Collectors.toMap(
                GraphElementViewModel::getType,
                viewModel -> viewModel,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                FXCollections::observableHashMap));
    }

    private ObservableMap<ProcessElementType, EdgeViewModel> createEdges(Map<Integer, VertexViewModel> vertexes, List<TransitionData> transitions) {
        return transitions.stream().map(transitionData -> {
            ProcessElementType type = ProcessElementType.of(transitionData.getPluginId(), transitionData.getTitle());

            EdgeViewModel edgeViewModel = new EdgeViewModel();
            edgeViewModel.setId(transitionData.getId());
            edgeViewModel.setType(type);
            edgeViewModel.setVertexSource(vertexes.get(transitionData.getSourceId()));
            edgeViewModel.setVertexTarget(vertexes.get(transitionData.getTargetId()));

            EdgeFactoryWrapper edgeFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getEdgeFactories().get(type.getElementName());
            edgeViewModel.setData(edgeFactoryWrapper.getFactory().getElementDataConverter().importNodeData(transitionData.getAttributes()));

            return edgeViewModel;
        }).collect(Collectors.toMap(
                GraphElementViewModel::getType,
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
                processViewModel.getProcessName(),
                processViewModel.getDescription(),
                createElements(processViewModel.getVertexes()),
                createTransitions(processViewModel.getEdges()),
                processViewModel.getStartVertexId()
        );
    }

    //todo тут ошибки. Надо только аттрибуты отдавать на откуп плагинам
    private List<NodeData> createElements(ObservableMap<ProcessElementType, VertexViewModel> vertexes) {
        return vertexes.values().stream().map((Function<VertexViewModel, NodeData>)(v) -> {
            ProcessElementType type = v.getType();
            VertexFactoryWrapper vertexFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getVertexFactories().get(type.getElementName());
            ElementDataConverter elementDataConverter = vertexFactoryWrapper.getFactory().getElementDataConverter();
            return new NodeData(v.getId(), type.getPluginId(), type.getElementName(), elementDataConverter.exportNodeData(v.getData()), v.getClassName(), v.getDescription());
        }).collect(Collectors.toList());
    }

    //todo аналогично как и выше
    private List<TransitionData> createTransitions(ObservableMap<ProcessElementType, EdgeViewModel> edges) {
        return edges.values().stream().map((Function<EdgeViewModel, TransitionData>)(e) -> {
            ProcessElementType type = e.getType();
            EdgeFactoryWrapper edgeFactoryWrapper = store.getProcessElements().get(type.getPluginId()).getEdgeFactories().get(type.getElementName());
            return new TransitionData(
                    e.getId(),
                    type.getPluginId(),
                    type.getElementName(),
                    e.getDescription(),
                    edgeFactoryWrapper.getFactory().getElementDataConverter().exportNodeData(e.getData()),
                    e.getVertexSource().getId(),
                    e.getVertexTarget().getId(),
                    e.getTag()
            );
        }).collect(Collectors.toList());
    }

}
