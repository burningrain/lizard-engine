package com.github.burningrain.gvizfx.input;

import javafx.scene.input.InputEvent;

import java.util.HashMap;

public class InputEventManager {

    private final HashMap<String, GraphViewAction> codes = new HashMap<>();

    public void register(String action, GraphViewAction keyEventPredicate) {
        if(codes.containsKey(action)) {
            throw new IllegalStateException("Action [" + action + "] is already registered");
        }
        codes.put(action, keyEventPredicate);
    }

    public void unregister(String action) {
        codes.remove(action);
    }

    public boolean match(String action, InputEvent event) {
        GraphViewAction predicate = codes.get(action);
        if(predicate == null) {
            return false;
        }
        boolean isSupport = false;
        for (Class<? extends InputEvent> supportedEvent : predicate.getSupportedEvents()) {
            if(supportedEvent.isAssignableFrom(event.getClass())) {
                isSupport = true;
                break;
            }
        }

        return isSupport && predicate.test(event);
    }

}
