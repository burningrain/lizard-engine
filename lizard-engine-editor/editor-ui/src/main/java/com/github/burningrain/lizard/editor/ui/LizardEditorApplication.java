package com.github.burningrain.lizard.editor.ui;

import com.github.burningrain.lizard.editor.ui.config.*;
import com.github.burningrain.lizard.editor.ui.core.FxApplication;


public class LizardEditorApplication extends FxApplication {

    public static void main(String[] args) {
        FxApplication.arguments = args;
        launch(args);
    }

    @Override
    protected Class[] sources() {
        return new Class[]{
                ModelConfig.class,
                IOConfig.class,
                UiConfig.class,
                ComponentsConfig.class,
                ActionsConfig.class,
                DragAndDropConfigs.class,
                ApiConfig.class
        };
    }

}
