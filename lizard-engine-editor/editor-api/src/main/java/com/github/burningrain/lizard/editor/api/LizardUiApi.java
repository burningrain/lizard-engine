package com.github.burningrain.lizard.editor.api;

import com.github.burningrain.gvizfx.GraphView;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.function.Consumer;

public interface LizardUiApi {

    GraphView getGraphView();

    void createStageChild(Consumer<Stage> consumer);

    <T extends Serializable> T getDataModelForCurrentProject(String key);

}
