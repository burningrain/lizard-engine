package com.github.burningrain.lizard.editor.ui.components.editor.mode;

public interface ProcessEditorFsm {

    States getCurrentState();

    void changeState(States newState);

    void dispose();

}
