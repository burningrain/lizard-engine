package com.github.burningrain.lizard.editor.ui.io;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ParamUtils {

    private ParamUtils(){}

    public static <T> T mandatory(T value) {
        return Objects.requireNonNull(value);
    }

    public static <T> T optional(T value, T defaultValue) {
        Objects.requireNonNull(defaultValue);
        return value != null? value : defaultValue;
    }

    public static <T, R> R optional(T value, Function<T, R> function) {
        if(value == null) {
            return null;
        }
        return function.apply(value);
    }

    public static <T> T optional(T value) {
        return value;
    }

    public static <T> void ifNotNull(T value, Consumer<T> consumer) {
        if(value != null) {
            consumer.accept(value);
        }
    }



}
