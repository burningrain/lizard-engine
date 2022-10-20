package com.github.burningrain.lizard.engine.core;

public interface Converter<IN, OUT> {

    OUT convert(IN in);

}
