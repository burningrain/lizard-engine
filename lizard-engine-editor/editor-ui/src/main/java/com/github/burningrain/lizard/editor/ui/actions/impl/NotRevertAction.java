package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.core.action.Action;

public interface NotRevertAction extends Action {


    default boolean isNeedSave() {
        return false;
    }

    default boolean isCanRevert() {
        return false;
    }

    default void revert(){}


}
