package com.github.burningrain.lizard.editor.ui.core.action;

public class RedoAction implements Action {

    @Override
    public boolean isNeedSave() {
        return false;
    }

    @Override
    public String getId() {
        return "Redo";
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isCanRevert() {
        return false;
    }

    @Override
    public void revert() {
    }

}
