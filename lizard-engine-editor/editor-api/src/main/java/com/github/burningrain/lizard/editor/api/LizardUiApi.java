package com.github.burningrain.lizard.editor.api;

import javafx.stage.Stage;

import java.util.function.Consumer;

public interface LizardUiApi {

    void createStageChild(Consumer<Stage> consumer);

}
