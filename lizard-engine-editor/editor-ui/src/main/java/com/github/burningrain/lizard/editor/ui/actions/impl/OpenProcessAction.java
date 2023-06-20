package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.io.ProjectConverter;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;

@Scope("prototype")
@Component
public class OpenProcessAction implements NotRevertAction {

    @Autowired
    private ProjectConverter projectConverter;

    @Autowired
    private UiUtils uiUtils;

    @Autowired
    private Store store;

    @Override
    public String getId() {
        return Actions.OPEN_PROCESS;
    }

    @Override
    public void execute() {
        uiUtils.showOpenDialogChooserFile("Открыть файл процесса", "Файл процесса", Collections.singletonList("*.lzd"), this::handle);
    }

    private void handle(File file) {
        try {
            store.changeCurrentProject(projectConverter.importProject(file));
        } catch (Exception e) {
            throw new RuntimeException(e); //todo
        }
    }

}
