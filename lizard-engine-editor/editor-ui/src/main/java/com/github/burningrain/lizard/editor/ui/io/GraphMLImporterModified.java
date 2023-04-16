package com.github.burningrain.lizard.editor.ui.io;

import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.alg.util.Triple;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.graphml.GraphMLEventDrivenImporter;
import org.jgrapht.nio.graphml.GraphMLImporter;

import java.io.Reader;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class GraphMLImporterModified<V, E> extends GraphMLImporter<V, E> {

    public static final String DEFAULT_VERTEX_ID_KEY = "ID";

    // special attributes
    private static final String EDGE_WEIGHT_DEFAULT_ATTRIBUTE_NAME = "weight";
    private String edgeWeightAttributeName = EDGE_WEIGHT_DEFAULT_ATTRIBUTE_NAME;
    private boolean schemaValidation;
    private Function<String, V> vertexFactory;

    public GraphMLImporterModified() {
        this.schemaValidation = true;
    }

    public String getEdgeWeightAttributeName() {
        return edgeWeightAttributeName;
    }

    public void setEdgeWeightAttributeName(String edgeWeightAttributeName) {
        if (edgeWeightAttributeName == null) {
            throw new IllegalArgumentException("Edge weight attribute name cannot be null");
        }
        this.edgeWeightAttributeName = edgeWeightAttributeName;
    }

    public boolean isSchemaValidation() {
        return schemaValidation;
    }

    public void setSchemaValidation(boolean schemaValidation) {
        this.schemaValidation = schemaValidation;
    }

    public Function<String, V> getVertexFactory() {
        return vertexFactory;
    }

    public void setVertexFactory(Function<String, V> vertexFactory) {
        this.vertexFactory = vertexFactory;
    }


    //modified
    private BiConsumer<V, Map<String, Attribute>> vertexConsumer;
    private BiConsumer<E, Triple<V, V, Map<String, Attribute>>> edgeConsumer;

    public void addVertexWithAttributesMapConsumer(BiConsumer<V, Map<String, Attribute>> vertexConsumer) {
        this.vertexConsumer = vertexConsumer;
    }

    public void addEdgeWithAttributesMapConsumer(BiConsumer<E, Triple<V, V, Map<String, Attribute>>> edgeConsumer) {
        this.edgeConsumer = edgeConsumer;
    }
    //modified

    @Override
    public void importGraph(Graph<V, E> graph, Reader input) {
        GraphMLEventDrivenImporter genericImporter = new GraphMLEventDrivenImporter();
        genericImporter.setEdgeWeightAttributeName(edgeWeightAttributeName);
        genericImporter.setSchemaValidation(schemaValidation);

        Consumers globalConsumer = new Consumers(graph);
        genericImporter.addGraphAttributeConsumer(globalConsumer.graphAttributeConsumer);
        genericImporter.addVertexAttributeConsumer(globalConsumer.vertexAttributeConsumer);
        genericImporter.addEdgeAttributeConsumer(globalConsumer.edgeAttributeConsumer);
        genericImporter.addVertexConsumer(globalConsumer.vertexConsumer);
        genericImporter.addEdgeConsumer(globalConsumer.edgeConsumer);
        genericImporter.importInput(input);

        // modified
        IdentityHashMap<V, Map<String, Attribute>> vertexAttributes = globalConsumer.vertexAttributes;
        vertexAttributes.forEach(vertexConsumer);

        IdentityHashMap<E, Map<String, Attribute>> edgesAttributes = globalConsumer.edgesAttributes;
        IdentityHashMap<E, Pair<V, V>> edges = globalConsumer.edges;
        edgesAttributes.forEach(new BiConsumer<E, Map<String, Attribute>>() {
            @Override
            public void accept(E e, Map<String, Attribute> map) {
                Pair<V, V> vertexes = edges.get(e);
                edgeConsumer.accept(e, new Triple<>(vertexes.getFirst(), vertexes.getSecond(), map));
            }
        });

    }

    private class Consumers {
        private Graph<V, E> graph;
        private Map<String, V> nodesMap;
        private E lastEdge;
        private Triple<String, String, Double> lastTriple;

        //modified
        private IdentityHashMap<V, Map<String, Attribute>> vertexAttributes = new IdentityHashMap<>();
        private IdentityHashMap<E, Map<String, Attribute>> edgesAttributes = new IdentityHashMap<>();
        private IdentityHashMap<E, Pair<V, V>> edges = new IdentityHashMap();

        public Consumers(Graph<V, E> graph) {
            this.graph = graph;
            this.nodesMap = new HashMap<>();
            this.lastEdge = null;
            this.lastTriple = null;
        }

        public final BiConsumer<String, Attribute> graphAttributeConsumer = (key, a) -> {
            notifyGraphAttribute(key, a);
        };

        public final BiConsumer<Pair<String, String>, Attribute> vertexAttributeConsumer =
                (vertexAndKey, a) -> {

                    String id = vertexAndKey.getFirst();

                    // формирую мапу аттрибутов
                    V vertex = mapNode(id);
                    Map<String, Attribute> map = vertexAttributes.get(vertex);
                    if(map == null) {
                        map = new HashMap<>();
                        vertexAttributes.put(vertex, map);
                    }
                    map.put(vertexAndKey.getSecond(), a);
                    // формирую мапу аттрибутов

                    notifyVertexAttribute(vertex, vertexAndKey.getSecond(), a);
                };

        public final BiConsumer<Pair<Triple<String, String, Double>, String>,
                Attribute> edgeAttributeConsumer = (edgeAndKey, a) -> {
            Triple<String, String, Double> qe = edgeAndKey.getFirst();

            if (qe == lastTriple) {
                if (qe.getThird() != null
                        && edgeWeightAttributeName.equals(edgeAndKey.getSecond())
                        && graph.getType().isWeighted()) {
                    graph.setEdgeWeight(lastEdge, qe.getThird());
                }

                // формирую мапу аттрибутов
                Map<String, Attribute> attributes = edgesAttributes.get(lastEdge);
                if(attributes == null) {
                    attributes = new HashMap<>();
                    edgesAttributes.put(lastEdge, attributes);
                }
                attributes.put(edgeAndKey.getSecond(), a);
                // формирую мапу аттрибутов

                notifyEdgeAttribute(lastEdge, edgeAndKey.getSecond(), a);
            }
        };

        public final Consumer<String> vertexConsumer = (vId) -> {
            V v = mapNode(vId);
            notifyVertex(v);
            // TODO modified notifyVertexAttribute(v, DEFAULT_VERTEX_ID_KEY, DefaultAttribute.createAttribute(vId));
        };

        public final Consumer<Triple<String, String, Double>> edgeConsumer = (qe) -> {
            if (lastTriple != qe) {
                String source = qe.getFirst();
                String target = qe.getSecond();
                Double weight = qe.getThird();

                V sourceVertex = mapNode(source);
                V targetVertex = mapNode(target);
                E e = graph.addEdge(sourceVertex, targetVertex);
                if (weight != null && graph.getType().isWeighted()) {
                    graph.setEdgeWeight(e, weight);
                }

                lastEdge = e;
                lastTriple = qe;

                edges.put(e, new Pair<>(sourceVertex, targetVertex));
                notifyEdge(lastEdge);
            }
        };

        private V mapNode(String vId) {
            V vertex = nodesMap.get(vId);
            if (vertex == null) {
                if (vertexFactory != null) {
                    vertex = vertexFactory.apply(vId);
                    graph.addVertex(vertex);
                } else {
                    vertex = graph.addVertex();
                }
                nodesMap.put(vId, vertex);
            }
            return vertex;
        }

    }


}
