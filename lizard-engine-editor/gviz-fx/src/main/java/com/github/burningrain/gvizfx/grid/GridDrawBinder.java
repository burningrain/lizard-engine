package com.github.burningrain.gvizfx.grid;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.property.GridProperty;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;
import com.github.burningrain.gvizfx.input.InputEventManager;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;

public class GridDrawBinder implements GraphHandlersBinder {

    private final GridProperty gridProperty;

    private ChangeListener<Number> hvalueListener;
    private ChangeListener<Number> vvalueListener;
    private InvalidationListener layoutBoundsListener;
    private InvalidationListener viewportBoundsListener;

    public GridDrawBinder(GridProperty gridProperty) {
        this.gridProperty = gridProperty;
    }

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager,
                      GraphViewProperty graphViewProperty) {
        hvalueListener = (observable, oldValue, newValue) -> {
            relocateAndResizeGrid(viewData);
        };
        vvalueListener = (observable, oldValue, newValue) -> {
            relocateAndResizeGrid(viewData);
        };
        layoutBoundsListener = observable -> {
            relocateAndResizeGrid(viewData);
        };
        viewportBoundsListener = observable -> {
            relocateAndResizeGrid(viewData);
        };

        ScrollPane scrollPane = viewData.getScrollPane();
        addListeners(scrollPane);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager,
                       GraphViewProperty graphViewProperty) {
        ScrollPane scrollPane = viewData.getScrollPane();
        removeListeners(scrollPane);
    }

    private void relocateAndResizeGrid(GraphViewData viewData) {
        ScrollPane scrollPane = viewData.getScrollPane();
        removeListeners(scrollPane);

        GridUtils.relocateAndResizeGrid(viewData, gridProperty.gridSpacing.get());

        addListeners(scrollPane);
    }

    private void addListeners(ScrollPane scrollPane) {
        scrollPane.hvalueProperty().addListener(hvalueListener);
        scrollPane.vvalueProperty().addListener(vvalueListener);
        scrollPane.layoutBoundsProperty().addListener(layoutBoundsListener);
        scrollPane.viewportBoundsProperty().addListener(viewportBoundsListener);
    }

    private void removeListeners(ScrollPane scrollPane) {
        scrollPane.hvalueProperty().removeListener(hvalueListener);
        scrollPane.vvalueProperty().removeListener(vvalueListener);
        scrollPane.layoutBoundsProperty().removeListener(layoutBoundsListener);
        scrollPane.viewportBoundsProperty().removeListener(viewportBoundsListener);
    }

}
