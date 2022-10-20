package com.github.burningrain.gvizfx.handlers.binders.selecting;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.property.SimpleGraphElementProperty;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.model.GraphElementWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.SetChangeListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SelectingElementBinder implements GraphHandlersBinder {

    private SetChangeListener<GraphElementWrapper> listener;

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        listener = new SetChangeListener<GraphElementWrapper>() {

            private GraphElementWrapper<?> selected;
            private Set<? extends GraphElementWrapper> oldSelectedSet = Collections.emptySet();

            @Override
            public void onChanged(Change<? extends GraphElementWrapper> change) {
                if (change.wasRemoved()) {
                    GraphElementWrapper graphElement = change.getElementRemoved();
                    graphElement.getElement().deselect();
                }

                if (change.wasAdded()) {
                    GraphElementWrapper elementWrapper = change.getElementAdded();

                    SimpleObjectProperty<SimpleGraphElementProperty.SelectionProperty.SelectionMode> selectionMode =
                            graphViewProperty.elementModelProperty.selectionProperty.selectionMode;

                    if(SimpleGraphElementProperty.SelectionProperty.SelectionMode.ONE == selectionMode.get()) {
                        // если выделение уже есть - снимаем
                        if (elementWrapper.equals(selected)) {
                            elementWrapper.getElement().deselect();
                            selected = null;
                        } else if (!oldSelectedSet.contains(elementWrapper)) {
                            elementWrapper.getElement().select();
                            selected = elementWrapper;
                        }
                    } else {
                        elementWrapper.getElement().select();
                    }
                }

                oldSelectedSet = new HashSet<>(change.getSet());
            }
        };

        viewData.getGraphViewModel().selectedElementsProperty().addListener(listener);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        viewData.getGraphViewModel().selectedElementsProperty().removeListener(listener);
    }

}
