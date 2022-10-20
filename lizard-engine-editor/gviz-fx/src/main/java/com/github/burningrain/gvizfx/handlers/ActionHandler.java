package com.github.burningrain.gvizfx.handlers;

import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class ActionHandler<T extends Event> implements EventHandler<T> {

    private ArrayList<Consumer<? super T>> listeners;

    public void addListener(Consumer<? super T> consumer) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(Objects.requireNonNull(consumer));
    }

    public void removeListener(Consumer<? super T> consumer) {
        listeners.remove(consumer);
    }

    @Override
    public void handle(T event) {
        notifyListeners(event);
    }

    private void notifyListeners(T event) {
        if (listeners != null && !listeners.isEmpty()) {
            for (Consumer<? super T> listener : listeners) {
                listener.accept(event);
                if(event.isConsumed()) {
                    break;
                }
            }
        }
    }

}