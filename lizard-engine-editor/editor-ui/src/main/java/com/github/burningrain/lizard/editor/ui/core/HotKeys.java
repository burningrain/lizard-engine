package com.github.burningrain.lizard.editor.ui.core;

import javafx.scene.input.KeyCodeCombination;
import com.github.burningrain.lizard.editor.ui.core.action.Action;

import java.util.Map;

public interface HotKeys {

    Map<KeyCodeCombination, Class<? extends Action>> getMapping();

}
