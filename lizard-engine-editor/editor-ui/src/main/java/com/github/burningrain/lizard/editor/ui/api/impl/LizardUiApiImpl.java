package com.github.burningrain.lizard.editor.ui.api.impl;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
        //TODO кривота
        return (T) ((Map<String, T>)store.getCurrentProjectModel().getProcessViewModel().getData()).get(key);
    }

    @Override
    public void addOnCloseRequest(Consumer<? super WindowEvent> onCloseRequest) {
        uiUtils.addOnCloseRequest(onCloseRequest);
    }

    @Override
    public void showOpenDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer) {
        uiUtils.showOpenDialogChooserFile(title, desc, exts, consumer);
    }

    @Override
    public void showSaveDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer) {
        uiUtils.showSaveDialogChooserFile(title, desc, exts, consumer);
    }

}
