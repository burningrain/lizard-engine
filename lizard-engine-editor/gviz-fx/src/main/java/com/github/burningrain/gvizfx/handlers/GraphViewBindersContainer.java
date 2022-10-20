package com.github.burningrain.gvizfx.handlers;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;

import java.util.*;

public class GraphViewBindersContainer {

    private final GraphViewHandlers graphViewHandlers;
    private final GraphViewData graphViewData;
    private final InputEventManager inputEventManager;
    private final GraphViewProperty graphViewProperty;

    private HashMap<Class<? extends GraphHandlersBinder>, GraphHandlersBinder> binders = new HashMap<>();
    private HashSet<Class<? extends GraphHandlersBinder>> activeBindersSet = new HashSet<>();

    public GraphViewBindersContainer(GraphViewData graphViewData, GraphViewHandlers graphViewHandlers,
                                     InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        this.graphViewData = graphViewData;
        this.graphViewHandlers = graphViewHandlers;
        this.inputEventManager = inputEventManager;
        this.graphViewProperty = graphViewProperty;
    }

    public void registerBinder(GraphHandlersBinder binder) {
        Objects.requireNonNull(binder);
        binders.put(binder.getClass(), binder);
    }

    public void unregisterBinder(Class<? extends GraphHandlersBinder> clazz) {
        cancel(clazz);
        binders.remove(clazz);
    }

    public void apply(Class<? extends GraphHandlersBinder> clazz) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(binders.get(clazz), "Binder [" + clazz + "] was not registered")
                .apply(graphViewData, graphViewHandlers, inputEventManager, graphViewProperty);
        activeBindersSet.add(clazz);
    }

    public void cancel(Class<? extends GraphHandlersBinder> clazz) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(binders.get(clazz), "Binder [" + clazz + "] was not registered")
                .cancel(graphViewData, graphViewHandlers, inputEventManager, graphViewProperty);
        activeBindersSet.remove(clazz);
    }

    public void registerBinderAndApply(GraphHandlersBinder binder) {
        registerBinder(binder);
        apply(binder.getClass());
    }

    public boolean isActive(Class<? extends GraphHandlersBinder> clazz) {
        return activeBindersSet.contains(clazz);
    }

    public boolean isRegistered(Class<? extends GraphHandlersBinder> clazz) {
        return binders.containsKey(clazz);
    }

    public Collection<Class<? extends GraphHandlersBinder>> getActiveBinders() {
        return activeBindersSet;
    }

    public Collection<Class<? extends GraphHandlersBinder>> getAllBinders() {
        return binders.keySet();
    }

}
