package com.github.burningrain.gvizfx.handlers.binders;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.input.DefaultActions;
import com.github.burningrain.gvizfx.input.InputEventManager;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;


public class MovingCanvasBinder implements GraphHandlersBinder {

    private final float velocity = 0.01f;

    private DragContext dragContext;

    private boolean isMovingCanvas = false;
    private EventHandler<MouseEvent> mousePressedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<MouseEvent> mouseMovedEventHandler;
    private EventHandler<MouseEvent> onDragDetectedEventHandler;

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        ScrollPane scrollPane = viewData.getScrollPane();
        dragContext = new DragContext();
        mousePressedEventHandler = event -> {
            if (inputEventManager.match(DefaultActions.MOVING_CANVAS, event)) {
                isMovingCanvas = true;

                dragContext.x = event.getX();
                dragContext.y = event.getY();

                event.consume();
                event.setDragDetect(true);
                scrollPane.getScene().setCursor(Cursor.MOVE);
            }
        };
        mouseReleasedEventHandler = event -> {
            if (inputEventManager.match(DefaultActions.MOVING_CANVAS, event)) {
                isMovingCanvas = false;
                event.consume();
                scrollPane.getScene().setCursor(Cursor.DEFAULT);
            }
        };

        onDragDetectedEventHandler = event -> {
            if (inputEventManager.match(DefaultActions.MOVING_CANVAS, event)) {
                scrollPane.startFullDrag();
            }
        };

        mouseMovedEventHandler = event -> {
            if (!isMovingCanvas || !inputEventManager.match(DefaultActions.MOVING_CANVAS, event)) return;

            double x = event.getX();
            double y = event.getY();

            double resultX = dragContext.x - x;
            double resultY = dragContext.y - y;

            Bounds viewportBounds = scrollPane.getViewportBounds();
            Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();

            scrollPane.hvalueProperty().set(scrollPane.getHvalue() + resultX * velocity * viewportBounds.getWidth() / layoutBounds.getWidth());
            scrollPane.vvalueProperty().set(scrollPane.getVvalue() + resultY * velocity * viewportBounds.getHeight() / layoutBounds.getHeight());

            dragContext.x = x;
            dragContext.y = y;
            //todo проконтролировать боковую скорость. Сейчас двигаться вбок быстрее, чем по прямой.
        };

        scrollPane.setOnMousePressed(mousePressedEventHandler);
        scrollPane.setOnMouseReleased(mouseReleasedEventHandler);
        scrollPane.setOnDragDetected(onDragDetectedEventHandler);
        scrollPane.setOnMouseDragOver(mouseMovedEventHandler);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        ScrollPane scrollPane = viewData.getScrollPane();
        Node content = scrollPane.getContent();
        content.setOnMousePressed(null);
        content.setOnMouseReleased(null);
        content.setOnDragDetected(null);
        content.setOnMouseDragOver(null);
        mousePressedEventHandler = null;
        mouseReleasedEventHandler = null;
        mouseMovedEventHandler = null;
        onDragDetectedEventHandler = null;
        dragContext = null;
    }

}
