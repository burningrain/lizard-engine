package com.github.burningrain.gvizfx.handlers.binders;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.input.DefaultActions;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.model.*;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class DeleteGraphElementBinder implements GraphHandlersBinder  {

    private EventHandler<KeyEvent> onKeyPressedEventHandler;
    private GraphElementWrapperVisitor graphElementWrapperVisitor;

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        GraphViewModel graphViewModel = viewData.getGraphViewModel();
        graphElementWrapperVisitor = new GraphElementWrapperVisitor() {
            @Override
            public void visit(VertexElementWrapper vertexElementWrapper) {
                for (EdgeElementWrapper edge : vertexElementWrapper.getEdges()) {
                    graphViewModel.edgesProperty().remove(edge.getElementId());
                }
                graphViewModel.verticesProperty().remove(vertexElementWrapper.getElementId());
            }

            @Override
            public void visit(EdgeElementWrapper edgeElementWrapper) {
                VertexElementWrapper source = edgeElementWrapper.getSource();
                VertexElementWrapper target = edgeElementWrapper.getTarget();
                source.getEdges().remove(edgeElementWrapper);
                target.getEdges().remove(edgeElementWrapper);
                graphViewModel.edgesProperty().remove(edgeElementWrapper.getElementId());
            }

            @Override
            public void visit(GraphElementWrapper graphElementWrapper) {
                throw new IllegalArgumentException("Unknown element [" + graphElementWrapper.getElement().getClass() + "]"); //todo может, и не нужно
            }
        };

        onKeyPressedEventHandler = keyEvent -> {
            if(inputEventManager.match(DefaultActions.DELETE_GRAPH_ELEMENT, keyEvent)) {
                ObservableSet<GraphElementWrapper> graphElements = graphViewModel.selectedElementsProperty().get();
                for (GraphElementWrapper graphElement : graphElements) {
                    graphElement.accept(graphElementWrapperVisitor);
                }
                graphViewModel.selectedElementsProperty().clear();
            }
        };
        viewData.getScrollPane().setOnKeyPressed(onKeyPressedEventHandler);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        viewData.getCanvas().setOnKeyTyped(null);
        onKeyPressedEventHandler = null;
        graphElementWrapperVisitor = null;
    }

}
