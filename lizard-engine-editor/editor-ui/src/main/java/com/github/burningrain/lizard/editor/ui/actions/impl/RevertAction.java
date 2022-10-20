package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.core.action.Action;

public interface RevertAction extends Action {

    default boolean isNeedSave() {
        return true;
    }

    default boolean isCanRevert() {
        return true;
    }

}
