package com.github.burningrain.lizard.editor.ui.core;

import com.github.burningrain.lizard.editor.ui.core.action.*;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

import java.util.Map;

public class HotKeysManager implements EventHandler<KeyEvent> {

    private final ActionManager actionManager;
    private final ActionFactory actionFactory;
    private final HotKeys hotKeys;

    public HotKeysManager(ActionManager actionManager, ActionFactory actionFactory, HotKeys hotKeys) {
        this.actionManager = actionManager;
        this.actionFactory = actionFactory;
        this.hotKeys = hotKeys;
    }

    @Override
    public void handle(KeyEvent event) {
        Class<? extends Action> actionClass = null;
        for (Map.Entry<KeyCodeCombination, Class<? extends Action>> entry : hotKeys.getMapping().entrySet()) {
            if (entry.getKey().match(event)) {
                actionClass = entry.getValue();
                break;
            }
        }

        if (actionClass == UndoAction.class) {
            actionManager.undoAction();
        } else if (actionClass == RedoAction.class) {
            actionManager.redoAction();
        } else if (actionClass != null) {
            actionManager.executeAction(actionFactory.createAction(actionClass));
        }
    }

}
