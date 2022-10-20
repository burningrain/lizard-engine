package com.github.burningrain.gvizfx.handlers.binders;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.handlers.GraphElementHandlers;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.input.InputEventManager;
import javafx.beans.property.ReadOnlyProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class FollowNodeStrategyBinder implements GraphHandlersBinder {

    private javafx.beans.value.ChangeListener<Number> changeXlistener;

    private javafx.beans.value.ChangeListener<Number> changeYlistener;

    private Consumer<MouseEvent> mouseDraggedEventConsumer;

    private Consumer<MouseEvent> mouseReleasedEventConsumer;

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        ScrollPane scrollPane = viewData.getScrollPane();

        changeXlistener = (observable, oldValue, newValue) -> {
            Node node = ((Node) ((ReadOnlyProperty) observable).getBean()); //fixme кривота, но нужны размеры объекта
            double nodeWidth = node.getBoundsInParent().getWidth();
            double newX = newValue.doubleValue();

            Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
            double minX = layoutBounds.getMinX();
            double layoutWidth = layoutBounds.getWidth();
            Bounds viewportBounds = scrollPane.getViewportBounds();
            double viewportWidth = viewportBounds.getWidth();

            double leftX = minX + (layoutWidth - viewportWidth) * scrollPane.getHvalue();
            double rightX = leftX + viewportWidth;

            if (newX <= leftX) {
                scrollPane.hvalueProperty().set((newX - minX) / (layoutWidth - viewportWidth));
            }
            if (newX + nodeWidth >= rightX) {
                scrollPane.hvalueProperty().set((newX + nodeWidth - minX - viewportWidth) / (layoutWidth - viewportWidth));
            }
        };
        changeYlistener = (observable, oldValue, newValue) -> {
            Node node = ((Node) ((ReadOnlyProperty) observable).getBean()); //fixme кривота, но нужны размеры объекта
            double nodeHeight = node.getBoundsInParent().getHeight();
            double newY = newValue.doubleValue();

            Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
            double minY = layoutBounds.getMinY();

            double layoutHeight = layoutBounds.getHeight();
            Bounds viewportBounds = scrollPane.getViewportBounds();
            double viewportHeight = viewportBounds.getHeight();

            double topY = minY + (layoutHeight - viewportHeight) * scrollPane.getVvalue();
            double bottomY = topY + viewportHeight;

            if (newY <= topY) {
                scrollPane.vvalueProperty().set((newY - minY) / (layoutHeight - viewportHeight));
            }
            if (newY + nodeHeight >= bottomY) {
                scrollPane.vvalueProperty().set((newY + nodeHeight - minY - viewportHeight) / (layoutHeight - viewportHeight));
            }
        };
        mouseDraggedEventConsumer = event -> {
            Node node = (Node) event.getSource();
            bindXYlistener(node);
        };
        mouseReleasedEventConsumer = event -> {
            Node node = (Node) event.getSource();
            unbindXYlistener(node);
        };

        GraphElementHandlers vertexHandlers = handlers.getVertexHandlers();

        vertexHandlers.getEventFilter(MouseEvent.MOUSE_DRAGGED).addListener(mouseDraggedEventConsumer);
        vertexHandlers.getEventFilter(MouseEvent.MOUSE_RELEASED).addListener(mouseReleasedEventConsumer);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        GraphElementHandlers vertexHandlers = handlers.getVertexHandlers();

        vertexHandlers.getEventFilter(MouseEvent.MOUSE_DRAGGED).removeListener(mouseDraggedEventConsumer);
        vertexHandlers.getEventFilter(MouseEvent.MOUSE_RELEASED).removeListener(mouseReleasedEventConsumer);
        //todo отвязать
        changeXlistener = null;
        changeYlistener = null;
        mouseDraggedEventConsumer = null;
        mouseReleasedEventConsumer = null;
    }

    private void bindXYlistener(Node elementNode) {
        elementNode.layoutXProperty().addListener(changeXlistener);
        elementNode.layoutYProperty().addListener(changeYlistener);
    }

    private void unbindXYlistener(Node elementNode) {
        elementNode.layoutXProperty().removeListener(changeXlistener);
        elementNode.layoutYProperty().removeListener(changeYlistener);
    }

}
