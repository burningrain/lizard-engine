package com.github.burningrain.lizard.editor.ui.api.impl;

import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.stage.Stage;
import com.github.burningrain.lizard.editor.api.LizardUiApi;

import java.util.function.Consumer;

public class LizardUiApiImpl implements LizardUiApi {

    private final UiUtils uiUtils;

    public LizardUiApiImpl(UiUtils uiUtils) {
        this.uiUtils = uiUtils;
    }

    @Override
    public void createStageChild(Consumer<Stage> consumer) {
        uiUtils.createStageChild(consumer);
    }

}
