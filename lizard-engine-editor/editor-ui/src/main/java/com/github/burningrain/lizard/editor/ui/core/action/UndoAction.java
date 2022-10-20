package com.github.burningrain.lizard.editor.ui.core.action;

public class UndoAction implements Action {

    @Override
    public boolean isNeedSave() {
        return false;
    }

    @Override
    public String getId() {
        return "Undo";
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
