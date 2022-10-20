package com.github.burningrain.gvizfx.handlers.binders;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;

public interface GraphHandlersBinder {

    void apply(GraphViewData viewData, GraphViewHandlers handlers,
               InputEventManager inputEventManager, GraphViewProperty graphViewProperty);

    void cancel(GraphViewData viewData, GraphViewHandlers handlers,
                InputEventManager inputEventManager, GraphViewProperty graphViewProperty);

}
