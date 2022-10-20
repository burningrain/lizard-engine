package com.github.burningrain.gvizfx.input;

import javafx.scene.input.*;

import java.util.Collections;
import java.util.List;

public interface DefaultActions {

    String BOX_SELECTION = "BOX_SELECTION";
    String MANY_ELEMENTS_SELECTION = "MANY_ELEMENTS_SELECTION";
    String DELETE_GRAPH_ELEMENT = "DELETE_ELEMENT";
    String MOVING_CANVAS = "MOVING_CANVAS";

    GraphViewAction BOX_SELECTION_ACTION = new GraphViewAction() {
        @Override
        public List<Class<? extends InputEvent>> getSupportedEvents() {
            return Collections.singletonList(MouseEvent.class);
        }

        @Override
        public boolean test(InputEvent inputEvent) {
            MouseEvent mouseEvent = (MouseEvent) inputEvent;
            return MouseButton.PRIMARY == mouseEvent.getButton();
        }
    };

    GraphViewAction MANY_ELEMENTS_SELECTION_ACTION = new GraphViewAction() {
        @Override
        public List<Class<? extends InputEvent>> getSupportedEvents() {
            return Collections.singletonList(MouseEvent.class);
        }

        @Override
        public boolean test(InputEvent inputEvent) {
            MouseEvent mouseEvent = (MouseEvent) inputEvent;
            return mouseEvent.isShiftDown();
        }
    };

    GraphViewAction DELETE_ELEMENT_ACTION = new GraphViewAction() {
        @Override
        public List<Class<? extends InputEvent>> getSupportedEvents() {
            return Collections.singletonList(KeyEvent.class);
        }

        @Override
        public boolean test(InputEvent event) {
            KeyEvent keyEvent = (KeyEvent) event;
            return KeyCode.DELETE == keyEvent.getCode();
        }
    };

    GraphViewAction MOVING_CANVAS_ACTION = new GraphViewAction() {

        @Override
        public List<Class<? extends InputEvent>> getSupportedEvents() {
            return Collections.singletonList(MouseEvent.class);
        }

        @Override
        public boolean test(InputEvent inputEvent) {
            MouseEvent mouseEvent = (MouseEvent) inputEvent;
            return MouseButton.SECONDARY == mouseEvent.getButton();
        }

    };
}
