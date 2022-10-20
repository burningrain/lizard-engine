package com.github.burningrain.lizard.editor.ui.core.action;

public interface Action {

    boolean isNeedSave();

    String getId();

    void execute();

    boolean isCanRevert();

    void revert();

}
