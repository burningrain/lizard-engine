package com.github.burningrain.lizard.editor.ui.core;

import javafx.stage.Stage;

import java.util.function.Consumer;

public interface StageManager {

    void start(Stage primaryStage);

    void activate();

    void afterActivation();

}
