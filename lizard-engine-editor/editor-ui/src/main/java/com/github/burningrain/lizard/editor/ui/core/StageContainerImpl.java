package com.github.burningrain.lizard.editor.ui.core;

import javafx.stage.Stage;

import java.util.function.Consumer;

public class StageContainerImpl implements StageContainer {

    private final Stage primaryStage;

    public StageContainerImpl(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void createStageChild(Consumer<Stage> consumer) {
        consumer.accept(primaryStage);
    }

}
