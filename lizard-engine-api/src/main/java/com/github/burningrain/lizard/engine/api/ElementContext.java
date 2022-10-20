package com.github.burningrain.lizard.engine.api;

import com.github.burningrain.lizard.engine.api.elements.NodeElement;

public interface ElementContext<PC extends ProcessContext, OUT> {

    NodeElement<PC, OUT> getElement(Class clazz);

}
