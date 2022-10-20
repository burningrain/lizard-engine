package com.github.burningrain.gvizfx.handlers;

import com.sun.javafx.event.DirectEvent;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.input.*;
import javafx.stage.WindowEvent;

import java.util.HashMap;
import java.util.Map;

public class GraphElementHandlers {

    private static final EventType[] eventTypes = {
            EventType.ROOT,
            Event.ANY,
            ScrollToEvent.ANY,
            ActionEvent.ANY,

            // *** INPUT
            InputEvent.ANY,

            TouchEvent.ANY,
            TouchEvent.TOUCH_MOVED,
            TouchEvent.TOUCH_PRESSED,
            TouchEvent.TOUCH_RELEASED,
            TouchEvent.TOUCH_STATIONARY,

            // gesture
            GestureEvent.ANY,

            RotateEvent.ANY,
            RotateEvent.ROTATE,
            RotateEvent.ROTATION_STARTED,
            RotateEvent.ROTATION_FINISHED,

            ZoomEvent.ANY,
            ZoomEvent.ZOOM,
            ZoomEvent.ZOOM_STARTED,
            ZoomEvent.ZOOM_FINISHED,

            ScrollEvent.ANY,
            ScrollEvent.SCROLL,
            ScrollEvent.SCROLL_STARTED,
            ScrollEvent.SCROLL_FINISHED,

            SwipeEvent.ANY,
            SwipeEvent.SWIPE_UP,
            SwipeEvent.SWIPE_RIGHT,
            SwipeEvent.SWIPE_DOWN,
            SwipeEvent.SWIPE_LEFT,
            // gesture

            MouseEvent.ANY,
            MouseEvent.DRAG_DETECTED,
            MouseEvent.MOUSE_CLICKED,
            MouseEvent.MOUSE_DRAGGED,
            MouseEvent.MOUSE_ENTERED,
            MouseEvent.MOUSE_ENTERED_TARGET,
            MouseEvent.MOUSE_EXITED,
            MouseEvent.MOUSE_EXITED_TARGET,
            MouseEvent.MOUSE_MOVED,
            MouseEvent.MOUSE_PRESSED,
            MouseEvent.MOUSE_RELEASED,

            InputMethodEvent.ANY,
            KeyEvent.ANY,
            KeyEvent.KEY_PRESSED,
            KeyEvent.KEY_RELEASED,
            KeyEvent.KEY_TYPED,

            DragEvent.ANY,
            DragEvent.DRAG_DONE,
            DragEvent.DRAG_DROPPED,
            DragEvent.DRAG_ENTERED,
            DragEvent.DRAG_ENTERED_TARGET,
            DragEvent.DRAG_EXITED,
            DragEvent.DRAG_EXITED_TARGET,
            DragEvent.DRAG_OVER,

            ContextMenuEvent.ANY,
            ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            // *** INPUT

            WindowEvent.ANY,
            WindowEvent.WINDOW_CLOSE_REQUEST,
            WindowEvent.WINDOW_HIDDEN,
            WindowEvent.WINDOW_HIDING,
            WindowEvent.WINDOW_SHOWING,
            WindowEvent.WINDOW_SHOWN,

            DirectEvent.ANY,
            DirectEvent.DIRECT,

            WorkerStateEvent.ANY,
            WorkerStateEvent.WORKER_STATE_CANCELLED,
            WorkerStateEvent.WORKER_STATE_FAILED,
            WorkerStateEvent.WORKER_STATE_READY,
            WorkerStateEvent.WORKER_STATE_RUNNING,
            WorkerStateEvent.WORKER_STATE_SCHEDULED,
            WorkerStateEvent.WORKER_STATE_SUCCEEDED,
    };

    private final HashMap<EventType<? extends Event>, EventHandler<? super Event>> eventHandlers = new HashMap<>();
    private final HashMap<EventType<? extends Event>, ActionHandler<? super Event>> eventFilters = new HashMap<>();

    private final ActionHandler<MouseEvent> onMouseClickedHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMousePressedHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseReleasedHandler = new ActionHandler<>();

    private final ActionHandler<MouseEvent> onMouseEnteredHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseMovedHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseExitedHandler = new ActionHandler<>();

    private final ActionHandler<MouseEvent> onDragDetectedHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseDraggedHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseDragEnteredHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseDragExitedHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseDragOverHandler = new ActionHandler<>();
    private final ActionHandler<MouseEvent> onMouseDragReleasedHandler = new ActionHandler<>();

    private final ActionHandler<ContextMenuEvent> onContextMenuRequestedHandler = new ActionHandler<>();

    private final ActionHandler<DragEvent> onDragEnteredHandler = new ActionHandler<>();
    private final ActionHandler<DragEvent> onDragExitedHandler = new ActionHandler<>();
    private final ActionHandler<DragEvent> onDragDroppedHandler = new ActionHandler<>();
    private final ActionHandler<DragEvent> onDragOverHandler = new ActionHandler<>();
    private final ActionHandler<DragEvent> onDragDoneHandler = new ActionHandler<>();

    private final ActionHandler<KeyEvent> onKeyPressedHandler = new ActionHandler<>();
    private final ActionHandler<KeyEvent> onKeyTypedHandler = new ActionHandler<>();
    private final ActionHandler<KeyEvent> onKeyReleasedHandler = new ActionHandler<>();
    private final ActionHandler<InputMethodEvent> onInputMethodTextChangedHandler = new ActionHandler<>();

    {
        for (EventType eventType : eventTypes) {
            eventHandlers.put(eventType, new ActionHandler<>());
            eventFilters.put(eventType, new ActionHandler<>());
        }
    }

    public <T extends Event> void getEventHandler(EventType<T> eventType) {
        eventHandlers.get(eventType);
    }

    public final <T extends Event> ActionHandler<T> getEventFilter(EventType<T> eventType) {
        return (ActionHandler<T>) capture(eventFilters.get(eventType));
    }

    //todo разобраться с дженериками!!!
    private static ActionHandler<?> capture(ActionHandler<?> actionHandler) {
        return actionHandler;
    }

    //todo вынести наверх эти методы, чтобы оставить объект чистым dto
    public void applyHandlers(final Node node) {
        for (Map.Entry<EventType<? extends Event>, EventHandler<? super Event>> entry : eventHandlers.entrySet()) {
            node.addEventHandler(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<EventType<? extends Event>, ActionHandler<? super Event>> entry : eventFilters.entrySet()) {
            node.addEventFilter(entry.getKey(), entry.getValue());
        }

        node.setOnMouseClicked(onMouseClickedHandler);
        node.setOnMousePressed(onMousePressedHandler);
        node.setOnMouseReleased(onMouseReleasedHandler);

        node.setOnMouseEntered(onMouseEnteredHandler);
        node.setOnMouseMoved(onMouseMovedHandler);
        node.setOnMouseExited(onMouseExitedHandler);

        node.setOnDragDetected(onDragDetectedHandler);
        node.setOnMouseDragged(onMouseDraggedHandler);
        node.setOnMouseDragEntered(onMouseDragEnteredHandler);
        node.setOnMouseDragExited(onMouseDragExitedHandler);
        node.setOnMouseDragOver(onMouseDragOverHandler);
        node.setOnMouseDragReleased(onMouseDragReleasedHandler);

        node.setOnContextMenuRequested(onContextMenuRequestedHandler);

        node.setOnDragEntered(onDragEnteredHandler);
        node.setOnDragExited(onDragExitedHandler);
        node.setOnDragDropped(onDragDroppedHandler);
        node.setOnDragOver(onDragOverHandler);
        node.setOnDragDone(onDragDoneHandler);

        node.setOnKeyPressed(onKeyPressedHandler);
        node.setOnKeyTyped(onKeyTypedHandler);
        node.setOnKeyReleased(onKeyReleasedHandler);
        node.setOnInputMethodTextChanged(onInputMethodTextChangedHandler);
    }

    public void removeHandlers(Node node) {
        node.setOnMouseClicked(null);
        node.setOnMousePressed(null);
        node.setOnMouseReleased(null);

        node.setOnMouseEntered(null);
        node.setOnMouseMoved(null);
        node.setOnMouseExited(null);

        node.setOnMouseDragged(null);
        node.setOnMouseDragEntered(null);
        node.setOnMouseDragExited(null);
        node.setOnMouseDragOver(null);
        node.setOnMouseDragReleased(null);

        node.setOnContextMenuRequested(null);

        node.setOnDragDetected(null);
        node.setOnDragEntered(null);
        node.setOnDragExited(null);
        node.setOnDragDropped(null);
        node.setOnDragOver(null);
        node.setOnDragDone(null);

        node.setOnKeyPressed(null);
        node.setOnKeyTyped(null);
        node.setOnKeyReleased(null);
        node.setOnInputMethodTextChanged(null);
    }
    //todo вынести наверх эти методы, чтобы оставить объект чистым dto

    public ActionHandler<MouseEvent> getOnMouseClickedHandler() {
        return onMouseClickedHandler;
    }

    public ActionHandler<MouseEvent> getOnMousePressedHandler() {
        return onMousePressedHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseReleasedHandler() {
        return onMouseReleasedHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseEnteredHandler() {
        return onMouseEnteredHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseMovedHandler() {
        return onMouseMovedHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseExitedHandler() {
        return onMouseExitedHandler;
    }

    public ActionHandler<MouseEvent> getOnDragDetectedHandler() {
        return onDragDetectedHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseDraggedHandler() {
        return onMouseDraggedHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseDragEnteredHandler() {
        return onMouseDragEnteredHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseDragExitedHandler() {
        return onMouseDragExitedHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseDragOverHandler() {
        return onMouseDragOverHandler;
    }

    public ActionHandler<MouseEvent> getOnMouseDragReleasedHandler() {
        return onMouseDragReleasedHandler;
    }

    public ActionHandler<ContextMenuEvent> getOnContextMenuRequestedHandler() {
        return onContextMenuRequestedHandler;
    }

    public ActionHandler<DragEvent> getOnDragEnteredHandler() {
        return onDragEnteredHandler;
    }

    public ActionHandler<DragEvent> getOnDragExitedHandler() {
        return onDragExitedHandler;
    }

    public ActionHandler<DragEvent> getOnDragDroppedHandler() {
        return onDragDroppedHandler;
    }

    public ActionHandler<DragEvent> getOnDragOverHandler() {
        return onDragOverHandler;
    }

    public ActionHandler<DragEvent> getOnDragDoneHandler() {
        return onDragDoneHandler;
    }

    public ActionHandler<KeyEvent> getOnKeyPressedHandler() {
        return onKeyPressedHandler;
    }

    public ActionHandler<KeyEvent> getOnKeyTypedHandler() {
        return onKeyTypedHandler;
    }

    public ActionHandler<KeyEvent> getOnKeyReleasedHandler() {
        return onKeyReleasedHandler;
    }

    public ActionHandler<InputMethodEvent> getOnInputMethodTextChangedHandler() {
        return onInputMethodTextChangedHandler;
    }

}
