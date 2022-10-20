package com.github.burningrain.lizard.editor.ui.core.action;

import java.util.EmptyStackException;

public class ActionManager {

    private CircularLifoBuffer<Action> actions = new CircularLifoBuffer(20); // todo сделать настраиваемым параметром
    private CircularLifoBuffer<Action> redoActions = new CircularLifoBuffer(20); // todo сделать настраиваемым параметром

    public void executeAction(Action action) {
        action.execute();
        if(action.isNeedSave()) {
            actions.push(action);
        }
    }

    public void undoAction() {
        Action action;
        try {
            action = actions.pop();
        } catch (EmptyStackException e) {
            return;
        }
        if(action == null) {
            return;
        }

        if(action.isCanRevert()) {
            action.revert();
        }
        redoActions.add(action);
    }

    public void redoAction() {
        Action action;
        try {
            action = redoActions.pop();
        } catch (EmptyStackException e) {
            return;
        }
        if(action != null) {
            executeAction(action);
        }
    }

}
