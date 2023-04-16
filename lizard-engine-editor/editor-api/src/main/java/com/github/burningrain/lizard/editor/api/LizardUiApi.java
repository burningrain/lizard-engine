package com.github.burningrain.lizard.editor.api;

import javafx.stage.Stage;

import java.io.Serializable;
import java.util.function.Consumer;

public interface LizardUiApi {

    void createStageChild(Consumer<Stage> consumer);

    <T extends Serializable> T getDataModelForCurrentProject(String key);

}
