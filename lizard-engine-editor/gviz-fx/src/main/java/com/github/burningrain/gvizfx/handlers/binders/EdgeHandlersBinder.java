package com.github.burningrain.gvizfx.handlers.binders;

import com.github.burningrain.gvizfx.*;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.element.EdgeElement;
import com.github.burningrain.gvizfx.element.GraphElement;
import com.github.burningrain.gvizfx.handlers.GraphElementHandlers;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.input.DefaultActions;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.model.GraphViewModel;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class EdgeHandlersBinder implements GraphHandlersBinder {

    private Consumer<MouseEvent> onMousePressedEventHandler;
    private Consumer<MouseEvent> onMouseClickedEventHandler;

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        onMousePressedEventHandler = event -> {

            Node node = (Node) event.getSource();
            node.toFront();

//            double scale = graphView.getScale();
//
//            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
//            dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

            event.consume();
        };

        onMouseClickedEventHandler = event -> {
            GraphElement source = (GraphElement) event.getSource();

            GraphViewModel graphViewModel = viewData.getGraphViewModel();
            if (source instanceof EdgeElement) {
                //todo
                if (!inputEventManager.match(DefaultActions.MANY_ELEMENTS_SELECTION, event)) {
                    graphViewModel.selectedElementsProperty().clear();
                }
                graphViewModel.selectedElementsProperty().add(graphViewModel.getEdges().get(source.getGraphElementId()));
                event.consume();
            }
        };

        GraphElementHandlers edgeHandlers = handlers.getEdgeHandlers();

        edgeHandlers.getOnMousePressedHandler().addListener(onMousePressedEventHandler);
        edgeHandlers.getOnMouseClickedHandler().addListener(onMouseClickedEventHandler);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        GraphElementHandlers edgeHandlers = handlers.getEdgeHandlers();

        edgeHandlers.getOnMousePressedHandler().removeListener(onMousePressedEventHandler);
        edgeHandlers.getOnMouseClickedHandler().removeListener(onMouseClickedEventHandler);
    }

}
