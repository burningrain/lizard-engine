package com.github.burningrain.gvizfx.handlers.binders.selecting;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.property.SimpleGraphElementProperty;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;
import com.github.burningrain.gvizfx.input.DefaultActions;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.model.EdgeElementWrapper;
import com.github.burningrain.gvizfx.model.GraphViewModel;
import com.github.burningrain.gvizfx.model.VertexElementWrapper;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class SelectingBoxBinder implements GraphHandlersBinder {

    private EventHandler<MouseEvent> mousePressedEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;

    private final Region rectangle = new Region() {{
        setVisible(false);
        setMouseTransparent(true);
    }};
    private boolean isDrawSelectionBox = false;

    private double startX;
    private double startY;

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        rectangle.borderProperty().bind(graphViewProperty.elementModelProperty.selectionProperty.selectionBorder);
        rectangle.backgroundProperty().bind(graphViewProperty.elementModelProperty.selectionProperty.selectionBackground);
        mousePressedEventHandler = event -> {
            if (inputEventManager.match(DefaultActions.BOX_SELECTION, event)) {
                isDrawSelectionBox = true;
                graphViewProperty.elementModelProperty.selectionProperty.selectionMode.set(
                        SimpleGraphElementProperty.SelectionProperty.SelectionMode.MANY);

                rectangle.setVisible(true);
                rectangle.setMouseTransparent(false);
                rectangle.setMaxWidth(USE_COMPUTED_SIZE);
                rectangle.setMaxHeight(USE_COMPUTED_SIZE);

                rectangle.setLayoutX(event.getX());
                rectangle.setLayoutY(event.getY());

                startX = event.getX();
                startY = event.getY();
                drawRegion(startX, startY, startX + 1, startY + 1);

                event.consume();
            }
        };
        mouseDraggedEventHandler = event -> {
            if (!isDrawSelectionBox) return;

            double endX = (event.getX() >= 0)? event.getX() :
                    Math.max(viewData.getScrollPane().getContent().getLayoutBounds().getMinX(), event.getX());
            double endY = (event.getY() >= 0)? event.getY() :
                    Math.max(viewData.getScrollPane().getContent().getLayoutBounds().getMinY(), event.getY());
            drawRegion(startX, startY, endX, endY);
            event.consume();
        };
        mouseReleasedEventHandler = event -> {
            if (inputEventManager.match(DefaultActions.BOX_SELECTION, event)) {
                isDrawSelectionBox = false;

                GraphViewModel graphViewModel = viewData.getGraphViewModel();
                ObservableMap<String, VertexElementWrapper> vertices = graphViewModel.getVertices();

                graphViewModel.getSelectedElements().clear();
                for (VertexElementWrapper value : vertices.values()) {
                    Node node = value.getElement();
                    if (rectangle.getBoundsInParent().contains(node.getBoundsInParent())) {
                        graphViewModel.getSelectedElements().add(value);
                    }
                }
                ObservableMap<String, EdgeElementWrapper> edges = graphViewModel.getEdges();
                for (EdgeElementWrapper value : edges.values()) {
                    Node node = value.getElement();
                    if (rectangle.getBoundsInParent().contains(node.getBoundsInParent())) {
                        graphViewModel.getSelectedElements().add(value);
                    }
                }

                rectangle.setVisible(false);
                rectangle.setMouseTransparent(true);
                rectangle.setMaxWidth(1);
                rectangle.setMaxHeight(1);

                graphViewProperty.elementModelProperty.selectionProperty.selectionMode.set(
                        SimpleGraphElementProperty.SelectionProperty.SelectionMode.ONE);
                event.consume();
            }
        };

        Pane canvas = (Pane) viewData.getCanvas(); //FIXME bad cast
        canvas.getChildren().add(rectangle);

        canvas.setOnMousePressed(mousePressedEventHandler);
        canvas.setOnMouseDragged(mouseDraggedEventHandler);
        canvas.setOnMouseReleased(mouseReleasedEventHandler);
    }

    private void drawRegion(double startX, double startY, double endX, double endY) {
        double leftUpX = Math.min(startX, endX);
        double leftUpY = Math.min(startY, endY);

        double width = Math.abs(endX - startX);
        double height = Math.abs(endY - startY);

        rectangle.setLayoutX(leftUpX);
        rectangle.setLayoutY(leftUpY);
        rectangle.setMinWidth(width);
        rectangle.setMinHeight(height);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        rectangle.borderProperty().unbind();
        rectangle.backgroundProperty().unbind();

        Parent pane = viewData.getScrollPane();
        pane.setOnMousePressed(null);
        pane.setOnMouseReleased(null);
        mousePressedEventHandler = null;
        mouseDraggedEventHandler = null;
        mouseReleasedEventHandler = null;
    }

}
