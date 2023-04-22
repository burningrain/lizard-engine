package com.github.burningrain.lizard.editor.ui.api.impl;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.stage.Stage;
import com.github.burningrain.lizard.editor.api.LizardUiApi;

import java.io.Serializable;
import java.util.function.Consumer;

public class LizardUiApiImpl implements LizardUiApi {

    private final UiUtils uiUtils;
    private final Store store;
    private final GraphView graphView;

    public LizardUiApiImpl(UiUtils uiUtils, Store store, GraphView graphView) {
        this.uiUtils = uiUtils;
        this.store = store;
        this.graphView = graphView;
    }

    @Override
    public GraphView getGraphView() {
        return graphView;
    }

    @Override
    public void createStageChild(Consumer<Stage> consumer) {
        uiUtils.createStageChild(consumer);
    }

    @Override
    public <T extends Serializable> T getDataModelForCurrentProject(String key) {
        return (T) store.getCurrentProjectModel().getProcessViewModel().getData().get(key);
    }

}
