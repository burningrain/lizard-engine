package com.github.burningrain.gvizfx.input;

import javafx.scene.input.InputEvent;

import java.util.List;
import java.util.function.Predicate;

public interface GraphViewAction extends Predicate<InputEvent> {

    List<Class<? extends InputEvent>> getSupportedEvents();

}
