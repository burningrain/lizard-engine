package com.github.burningrain.lizard.editor.ui.core;

import javafx.stage.Stage;

import java.util.function.Consumer;

public interface StageContainer {

    void createStageChild(Consumer<Stage> consumer);

}
