package com.github.burningrain.lizard.engine.api;

public interface ProcessContextLoader<PC extends ProcessContext, IN> {

    PC getContext(IN in);

}
