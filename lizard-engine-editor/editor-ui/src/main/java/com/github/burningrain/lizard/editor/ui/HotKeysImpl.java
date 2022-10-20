package com.github.burningrain.lizard.editor.ui;

import com.github.burningrain.lizard.editor.ui.core.HotKeys;
import com.github.burningrain.lizard.editor.ui.core.action.Action;
import com.github.burningrain.lizard.editor.ui.core.action.RedoAction;
import com.github.burningrain.lizard.editor.ui.core.action.UndoAction;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HotKeysImpl implements HotKeys {

    @Override
    public Map<KeyCodeCombination, Class<? extends Action>> getMapping() {
        HashMap<KeyCodeCombination, Class<? extends Action>> map = new HashMap<>();
        map.put(new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN), UndoAction.class);
        map.put(new KeyCodeCombination(KeyCode.Z, KeyCodeCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN), RedoAction.class);

        return Collections.unmodifiableMap(map);
    }

}
