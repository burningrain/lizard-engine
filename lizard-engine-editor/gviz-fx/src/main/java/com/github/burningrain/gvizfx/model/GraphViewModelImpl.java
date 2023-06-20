package com.github.burningrain.gvizfx.model;

import com.github.burningrain.gvizfx.element.EdgeElement;
import com.github.burningrain.gvizfx.element.edge.EdgeFactory;
import com.github.burningrain.gvizfx.element.VertexElement;
import com.github.burningrain.gvizfx.property.SimpleGraphElementProperty;
import com.github.burningrain.gvizfx.element.edge.line.LineEdgeFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

import java.util.Objects;

public class GraphViewModelImpl implements GraphViewModel {

    private final SimpleGraphViewModelProperty simpleGraphViewModel = new SimpleGraphViewModelProperty();
    private final SimpleGraphElementProperty elementModel;

    private EdgeFactory<? extends EdgeElement> edgeFactory = new LineEdgeFactory();

    private final Scale scale = new Scale(1, 1, 1);

    public GraphViewModelImpl(SimpleGraphElementProperty graphElementModel) {
        this.elementModel = graphElementModel;
    }

    public <N extends Node> VertexElement<N> addVertex(VertexElement<N> node, double x, double y) {
        node.setLayoutX(x);
        node.setLayoutY(y);

        node.setElementModel(elementModel);

        simpleGraphViewModel.verticesProperty().put(node.getGraphElementId(), new VertexElementWrapper(node));
        node.setFocusTraversable(true);

        return node;
    }

    public <N extends Node> EdgeElement addEdge(String edgeId, boolean isDirectional, String sourceId, String targetId, N userNode) {
        Objects.requireNonNull(edgeId);
        Objects.requireNonNull(sourceId);
        Objects.requireNonNull(targetId);

        VertexElementWrapper source = simpleGraphViewModel.getVertices().get(sourceId);
        VertexElementWrapper target = simpleGraphViewModel.getVertices().get(targetId);
        EdgeElement edgeElement = edgeFactory.createEdge(edgeId, isDirectional, sourceId.equals(targetId), userNode);
        edgeElement.setElementModel(elementModel);

        EdgeElementWrapper edgeElementWrapper = new EdgeElementWrapper(edgeElement, source, target);
        source.getEdges().add(edgeElementWrapper);
        target.getEdges().add(edgeElementWrapper);
        simpleGraphViewModel.edgesProperty().put(edgeId, edgeElementWrapper);

        Node sourceNode = source.getElement();
        Node targetNode = target.getElement();
        //fixme ну хак же
        if (Validator.isNeedRunLater(sourceNode) || Validator.isNeedRunLater(targetNode)) {
            Platform.runLater(() -> edgeElement.bind(sourceNode, targetNode, scale));
        } else {
            edgeElement.bind(sourceNode, targetNode, scale);
        }

        return edgeElement;
    }

    public ObservableSet<GraphElementWrapper> getSelectedElements() {
        return simpleGraphViewModel.getSelectedElements();
    }

    public SimpleSetProperty<GraphElementWrapper> selectedElementsProperty() {
        return simpleGraphViewModel.selectedElementsProperty();
    }

    @Override
    public ObservableMap<String, VertexElementWrapper> getVertices() {
        return simpleGraphViewModel.getVertices();
    }

    @Override
    public SimpleMapProperty<String, VertexElementWrapper> verticesProperty() {
        return simpleGraphViewModel.verticesProperty();
    }

    public void setVertices(ObservableMap<String, VertexElementWrapper> vertices) {
        this.simpleGraphViewModel.setVertices(vertices);
    }

    @Override
    public ObservableMap<String, EdgeElementWrapper> getEdges() {
        return simpleGraphViewModel.getEdges();
    }

    @Override
    public SimpleMapProperty<String, EdgeElementWrapper> edgesProperty() {
        return simpleGraphViewModel.edgesProperty();
    }

    public void setEdges(ObservableMap<String, EdgeElementWrapper> edges) {
        this.simpleGraphViewModel.setEdges(edges);
    }

    public void setEdgeFactory(EdgeFactory<? extends EdgeElement> edgeFactory) {
        this.edgeFactory = edgeFactory;
    }

    public EdgeFactory<? extends EdgeElement> getEdgeFactory() {
        return edgeFactory;
    }

    @Override
    public void clear() {
        //todo
        simpleGraphViewModel.clear();
    }

    @Override
    public BooleanProperty isClearExecutedProperty() {
        return simpleGraphViewModel.isClearExecutedProperty();
    }

}
