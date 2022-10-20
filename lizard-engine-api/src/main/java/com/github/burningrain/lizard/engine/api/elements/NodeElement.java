package com.github.burningrain.lizard.engine.api.elements;

import com.github.burningrain.lizard.engine.api.ProcessContext;

public interface NodeElement<PC extends ProcessContext, OUT> {

    NodeElementResult<OUT> execute(PC context);

}
