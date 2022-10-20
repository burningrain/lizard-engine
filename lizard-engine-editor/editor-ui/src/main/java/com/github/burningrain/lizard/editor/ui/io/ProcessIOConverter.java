package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.ui.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.io.*;

import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.stream.Collectors;

import static com.github.burningrain.lizard.editor.ui.io.ParamUtils.*;

public class ProcessIOConverter {

    private static final Attribute EMPTY_ATTRIBUTE = DefaultAttribute.createAttribute("");

    private final Store store;

    public ProcessIOConverter(Store store) {
        this.store = store;
    }

    public ProcessViewModel importProcess(InputStream inputStream) throws ImportException {
        GraphMLImporter<VertexViewModel, EdgeViewModel> importer = new GraphMLImporter<>((s, map) -> {
            String pluginId = mandatory(map.get(GraphAttributes.PLUGIN_ID.name()).getValue());
            String type = mandatory(map.get(GraphAttributes.TYPE.name()).getValue());
            int id = Integer.parseInt(mandatory(map.get(GraphAttributes.ID.name()).getValue()));
            int coordX = Integer.parseInt(mandatory(map.get(GraphAttributes.COORD_X.name()).getValue()));
            int coordY = Integer.parseInt(mandatory(map.get(GraphAttributes.COORD_Y.name()).getValue()));
            String data = optional(map.get(GraphAttributes.DATA.name()), Attribute::getValue);
            String className = optional(map.get(GraphAttributes.CLASSNAME.name()), EMPTY_ATTRIBUTE).getValue();
            String description = optional(map.get(GraphAttributes.DESCRIPTION.name()), EMPTY_ATTRIBUTE).getValue();

            ProcessElementType processElementType = ProcessElementType.of(pluginId, type);
            VertexFactoryWrapper vertexFactoryWrapper = store.getVertexFactoryWrapper(processElementType);
            if(vertexFactoryWrapper == null) {
                vertexFactoryWrapper = VertexFactoryWrapper.createDefault(processElementType);
            }
            ElementDataConverter elementDataConverter = vertexFactoryWrapper.getFactory().getElementDataConverter();

            VertexViewModel viewModel = new VertexViewModel();
            viewModel.setId(id);
            viewModel.setType(processElementType);
            viewModel.setX(coordX);
            viewModel.setY(coordY);
            ifNotNull(data, (String d) -> {
                viewModel.setData(ObjectMapperUtils.createDataFromString(elementDataConverter, d));
            });

            viewModel.setClassName(className);
            viewModel.setDescription(description);

            return viewModel;
        }, (v1, v2, s, map) -> {
            String pluginId = mandatory(map.get(GraphAttributes.PLUGIN_ID.name()).getValue());
            String type = mandatory(map.get(GraphAttributes.TYPE.name()).getValue());
            int id = Integer.parseInt(mandatory(map.get(GraphAttributes.ID.name()).getValue()));
            String data = optional(map.get(GraphAttributes.DATA.name()), Attribute::getValue);
            String description = optional(map.get(GraphAttributes.DESCRIPTION.name()), EMPTY_ATTRIBUTE).getValue();
            String tag = optional(map.get(GraphAttributes.TAG.name()), EMPTY_ATTRIBUTE).getValue();

            ProcessElementType processElementType = ProcessElementType.of(pluginId, type);
            EdgeFactoryWrapper edgeFactoryWrapper = store.getEdgeFactoryWrapper(processElementType);
            if(edgeFactoryWrapper == null) {
                edgeFactoryWrapper = EdgeFactoryWrapper.createDefault(processElementType);
            }
            ElementDataConverter elementDataConverter = edgeFactoryWrapper.getFactory().getElementDataConverter();

            EdgeViewModel edgeViewModel = new EdgeViewModel();
            edgeViewModel.setId(id);
            edgeViewModel.setType(processElementType);
            edgeViewModel.setVertexSource(v1);
            edgeViewModel.setVertexTarget(v2);

            ifNotNull(data, d -> {
                edgeViewModel.setData(ObjectMapperUtils.createDataFromString(elementDataConverter, d));
            });

            edgeViewModel.setTag(tag);
            edgeViewModel.setDescription(description);

            return edgeViewModel;
        });

        DirectedPseudograph<VertexViewModel, EdgeViewModel> graph = new DirectedPseudograph<>(EdgeViewModel.class);
        importer.importGraph(graph, inputStream);

        ProcessViewModel processViewModel = new ProcessViewModel();

        ObservableMap<String, VertexViewModel> vertexes = graph.vertexSet().stream().collect(Collectors.toMap(
                v -> v.getId() + "", //todo кривота
                viewModel -> viewModel,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                FXCollections::observableHashMap
        ));

        ObservableMap<String, EdgeViewModel> edges = graph.edgeSet().stream().collect(Collectors.toMap(
                e -> e.getId() + "", //todo кривота
                viewModel -> {
                    // fixme добавляем ребра здесь, ну а как еще?
                    viewModel.getVertexSource().getEdges().add(viewModel);
                    viewModel.getVertexTarget().getEdges().add(viewModel);
                    return viewModel;
                },
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                FXCollections::observableHashMap
        ));

        processViewModel.setVertexes(vertexes);
        processViewModel.setEdges(edges);

        return processViewModel;
    }

    public byte[] exportProcess(ProcessViewModel currentProcessVM) {
        DirectedPseudograph<VertexViewModel, EdgeViewModel> graph = new DirectedPseudograph<>(EdgeViewModel.class);

        currentProcessVM.getVertexes().values().forEach(o -> {
            VertexViewModel vm = (VertexViewModel) o;
            graph.addVertex(vm);
        });
        currentProcessVM.getEdges().values().forEach(o -> {
            EdgeViewModel em = (EdgeViewModel) o;
            graph.addEdge(em.getVertexSource(), em.getVertexTarget(), em);
        });

        return exportGraph(graph);
    }

    //todo если возникает ошибка, пишется пустой файл, поправить
    private byte[] exportGraph(DirectedPseudograph<VertexViewModel, EdgeViewModel> graph) {
        GraphMLExporter<VertexViewModel, EdgeViewModel> exporter = new GraphMLExporter<>();
        exporter.setExportEdgeWeights(false);

        //exporter.registerAttribute(GraphAttributes.PROCESS_NAME.name(), GraphMLExporter.AttributeCategory.GRAPH, AttributeType.STRING);

        // Общие
        exporter.registerAttribute(GraphAttributes.ID.name(), GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.PLUGIN_ID.name(), GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.TYPE.name(), GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.DESCRIPTION.name(), GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING, "");
        exporter.registerAttribute(GraphAttributes.DATA.name(), GraphMLExporter.AttributeCategory.ALL, AttributeType.STRING, null);

        // нода
        exporter.registerAttribute(GraphAttributes.COORD_X.name(), GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.COORD_Y.name(), GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.CLASSNAME.name(), GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING, "");

        // дуга
        exporter.registerAttribute(GraphAttributes.NODE_SOURCE.name(), GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);
        exporter.registerAttribute(GraphAttributes.NODE_TARGET.name(), GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);
        // думаю, если дуга всего одна, то тег можно не указывать
        exporter.registerAttribute(GraphAttributes.TAG.name(), GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING, "");

        exporter.setVertexAttributeProvider(vertexViewModel -> {
            Integer id = mandatory(vertexViewModel.getId());
            mandatory(vertexViewModel.getType());
            String pluginId = mandatory(vertexViewModel.getType().getPluginId());
            String name = mandatory(vertexViewModel.getType().getElementName());
            String className = optional(vertexViewModel.getClassName(), "");
            String description = optional(vertexViewModel.getDescription(), "");
            Serializable data = optional(vertexViewModel.getData());


            ProcessElementType type = vertexViewModel.getType();

            VertexFactoryWrapper vertexFactoryWrapper = store.getVertexFactoryWrapper(type);
            if(vertexFactoryWrapper == null) {
                vertexFactoryWrapper = VertexFactoryWrapper.createDefault(type);
            }
            ElementDataConverter elementDataConverter = vertexFactoryWrapper.getFactory().getElementDataConverter();

            HashMap<String, Attribute> result = new HashMap<>();

            result.put(GraphAttributes.ID.name(), DefaultAttribute.createAttribute(id));
            result.put(GraphAttributes.PLUGIN_ID.name(), DefaultAttribute.createAttribute(pluginId));
            result.put(GraphAttributes.TYPE.name(), DefaultAttribute.createAttribute(name));

            result.put(GraphAttributes.DESCRIPTION.name(), DefaultAttribute.createAttribute(description));

            result.put(GraphAttributes.COORD_X.name(), DefaultAttribute.createAttribute(vertexViewModel.getX()));
            result.put(GraphAttributes.COORD_Y.name(), DefaultAttribute.createAttribute(vertexViewModel.getY()));

            ifNotNull(data, d -> {
                if(elementDataConverter == null) {
                    //todo вывести предупреждение
                    return;
                }
                result.put(GraphAttributes.DATA.name(),
                        DefaultAttribute.createAttribute(ObjectMapperUtils.createDataFormModel(elementDataConverter, d)));
            });

            result.put(GraphAttributes.CLASSNAME.name(), DefaultAttribute.createAttribute(className));
            return result;
        });
        exporter.setEdgeAttributeProvider(edgeViewModel -> {
            Integer id = mandatory(edgeViewModel.getId());
            mandatory(edgeViewModel.getType());
            String pluginId = mandatory(edgeViewModel.getType().getPluginId());
            String name = mandatory(edgeViewModel.getType().getElementName());
            Integer vertexSourceId = mandatory(edgeViewModel.getVertexSource().getId());
            Integer vertexTargetId = mandatory(edgeViewModel.getVertexTarget().getId());
            String tag = optional(edgeViewModel.getTag(), "");
            String description = optional(edgeViewModel.getDescription(), "");
            Serializable data = optional(edgeViewModel.getData());

            ProcessElementType type = edgeViewModel.getType();
            EdgeFactoryWrapper edgeFactoryWrapper = store.getEdgeFactoryWrapper(type);
            if(edgeFactoryWrapper == null) {
                edgeFactoryWrapper = EdgeFactoryWrapper.createDefault(type);
            }
            ElementDataConverter elementDataConverter = edgeFactoryWrapper.getFactory().getElementDataConverter();

            HashMap<String, Attribute> result = new HashMap<>();
            result.put(GraphAttributes.ID.name(), DefaultAttribute.createAttribute(id));
            result.put(GraphAttributes.PLUGIN_ID.name(), DefaultAttribute.createAttribute(pluginId));
            result.put(GraphAttributes.TYPE.name(), DefaultAttribute.createAttribute(name));
            result.put(GraphAttributes.NODE_SOURCE.name(), DefaultAttribute.createAttribute(vertexSourceId));
            result.put(GraphAttributes.NODE_TARGET.name(), DefaultAttribute.createAttribute(vertexTargetId));
            result.put(GraphAttributes.TAG.name(), DefaultAttribute.createAttribute(tag));

            result.put(GraphAttributes.DESCRIPTION.name(), DefaultAttribute.createAttribute(description));
            ifNotNull(data, d -> {
                if(elementDataConverter == null) {
                    //todo вывести предупреждение
                    return;
                }
                result.put(GraphAttributes.DATA.name(), DefaultAttribute.createAttribute(ObjectMapperUtils.createDataFormModel(elementDataConverter, d)));
            });
            return result;
        });

        StringWriter writer = new StringWriter();
        try {
            exporter.exportGraph(graph, writer);
        } catch (ExportException e) {
            throw new RuntimeException(e);
        }

        return writer.toString().getBytes();
    }

}