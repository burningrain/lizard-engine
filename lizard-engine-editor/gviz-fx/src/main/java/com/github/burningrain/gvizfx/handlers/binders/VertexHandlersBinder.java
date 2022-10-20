package com.github.burningrain.gvizfx.handlers.binders;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.element.VertexElement;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.handlers.GraphElementHandlers;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.input.DefaultActions;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.model.*;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class VertexHandlersBinder implements GraphHandlersBinder {

    private Consumer<MouseEvent> onMousePressedEventHandler;

    private Consumer<MouseEvent> onMouseDraggedEventHandler;

    private Consumer<MouseEvent> onMouseClickedEventHandler;

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager,
                      GraphViewProperty graphViewProperty) {
        GraphViewModel graphViewModel = viewData.getGraphViewModel();

        onMousePressedEventHandler = event -> {

            Node node = (Node) event.getSource();
            node.toFront();

//            double scale = graphView.getScale();
//
//            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
//            dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

            event.consume();
        };

        onMouseDraggedEventHandler = event -> {

            Node node = (Node) event.getSource();
            // adjust the offset in case we are zoomed
//            double scale = graphView.getScale();
//
//            offsetX /= scale;
//            offsetY /= scale;

            double dX = event.getX() - node.getLayoutBounds().getWidth() / 2;
            double dY = event.getY() - node.getLayoutBounds().getHeight() / 2;
            if(graphViewProperty.gridProperty.isSnapToGrid.get()) {
                double spacing = graphViewProperty.gridProperty.gridSpacing.get();
                dX = (Math.abs(dX) < (spacing/2))? 0 : ((dX > 0)? spacing : -spacing);
                dY = (Math.abs(dY) < (spacing/2))? 0 : ((dY > 0)? spacing: -spacing);

                double layoutX = (int) Math.floor(node.getLayoutX());
                double layoutY = (int) Math.floor(node.getLayoutY());
                double residueX = layoutX % spacing;
                double residueY = layoutY % spacing;

                dX += (residueX > 0)? spacing - residueX : -residueX;
                dY += (residueY > 0)? spacing - residueY : -residueY;
            }
            final double deltaX = dX;
            final double deltaY = dY;

            GraphElementWrapperVisitor wrapperVisitor = new GraphElementWrapperVisitor() {
                @Override
                public void visit(VertexElementWrapper vertexElementWrapper) {
                    Node node = vertexElementWrapper.getElement();
                    double xCenter = (int)Math.floor(node.getLayoutX());
                    double yCenter = (int)Math.floor(node.getLayoutY());

                    node.relocate(xCenter + deltaX, yCenter + deltaY);
                }

                @Override
                public void visit(EdgeElementWrapper edgeElementWrapper) {
                    // auto recalculate
                }

                @Override
                public void visit(GraphElementWrapper graphElementWrapper) {
                    // do nothing because it is unknown element
                }
            };
            graphViewModel.getSelectedElements().forEach(graphElementWrapper -> {
                graphElementWrapper.accept(wrapperVisitor);
            });

            event.consume();
        };

        onMouseClickedEventHandler = event -> {
            Object source = event.getSource();

            if (source instanceof VertexElement) {
                if (!inputEventManager.match(DefaultActions.MANY_ELEMENTS_SELECTION, event)) {
                    graphViewModel.selectedElementsProperty().clear();
                }
                VertexElement element = (VertexElement) source;
                VertexElementWrapper vertexElementWrapper = graphViewModel.getVertices().get(element.getGraphElementId());
                graphViewModel.selectedElementsProperty().add(vertexElementWrapper);
                event.consume();
            }
        };

        GraphElementHandlers vertexHandlers = handlers.getVertexHandlers();

        vertexHandlers.getOnMousePressedHandler().addListener(onMousePressedEventHandler);
        vertexHandlers.getOnMouseDraggedHandler().addListener(onMouseDraggedEventHandler);
        vertexHandlers.getOnMouseClickedHandler().addListener(onMouseClickedEventHandler);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager,
                       GraphViewProperty graphViewProperty) {
        GraphElementHandlers vertexHandlers = handlers.getVertexHandlers();

        vertexHandlers.getOnMousePressedHandler().removeListener(onMousePressedEventHandler);
        vertexHandlers.getOnMouseDraggedHandler().removeListener(onMouseDraggedEventHandler);
        vertexHandlers.getOnMouseClickedHandler().removeListener(onMouseClickedEventHandler);
    }

}
