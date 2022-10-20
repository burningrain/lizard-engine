package com.github.burningrain.lizard.editor.ui;

import com.github.burningrain.lizard.editor.ui.actions.impl.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import com.github.burningrain.lizard.editor.ui.core.MenuInitializer;
import com.github.burningrain.lizard.editor.ui.core.action.Action;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;

public class MenuInitializerImpl implements MenuInitializer {

    private ActionManager actionManager;
    private ActionFactory actionFactory;

    public MenuInitializerImpl(ActionManager actionManager,
                               ActionFactory actionFactory
                               ) {
        this.actionManager = actionManager;
        this.actionFactory = actionFactory;
    }

    @Override
    public Menu[] getMenus() {
        return new Menu[]{
                createMenuFile(),
                createMenuSettings(),
                createMenuHelp(),
        };
    }

    private Menu createMenuFile() {
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(
                createMenuItem("open...", actionFactory.createAction(OpenProcessAction.class), new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN)),
                createMenuItem("save as...", actionFactory.createAction(SaveProcessAction.class), new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN)),
                createMenuItem("import...", actionFactory.createAction(ImportProcessAction.class), new KeyCodeCombination(KeyCode.I, KeyCodeCombination.CONTROL_DOWN)),
                createMenuItem("export...", actionFactory.createAction(ExportProcessAction.class), new KeyCodeCombination(KeyCode.E, KeyCodeCombination.CONTROL_DOWN))
        );

        return menuFile;
    }

    private Menu createMenuSettings() {
        Menu menuSettings = new Menu("Settings");
        menuSettings.getItems().addAll(
                createMenuItem("Grid", actionFactory.createAction(ShowGridSettingsAction.class), null)
        );

        return menuSettings;
    }

    private Menu createMenuHelp() {
        Menu menuHelp = new Menu("Help");
        menuHelp.getItems().addAll(
                createMenuItem("about", actionFactory.createAction(ShowAboutLizardEditorAction.class), null)
        );

        return menuHelp;
    }

    private MenuItem createMenuItem(String title, Action action, KeyCombination keyCombination) {
        MenuItem menuItem = new MenuItem(title);
        menuItem.setOnAction(event -> {
            actionManager.executeAction(action);
        });

        if (keyCombination != null) {
            menuItem.setAccelerator(keyCombination);
        }

        return menuItem;
    }

}
