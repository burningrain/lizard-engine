package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.api.LizardPluginApi;
import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;
import com.github.burningrain.lizard.editor.ui.io.ExportImportInnerConverter;
import com.github.burningrain.lizard.editor.ui.io.ProjectConverter;
import com.github.burningrain.lizard.editor.ui.io.ProjectModelImpl;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModelImpl;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.FileUtils;
import com.github.burningrain.lizard.editor.ui.utils.ProjectUtils;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.burningrain.lizard.editor.ui.actions.Actions.IMPORT_PROCESS;

@Scope("prototype")
@Component
public class ImportProcessAction implements NotRevertAction {

    @Autowired
    private UiUtils uiUtils;

    @Autowired
    private Store store;

    @Autowired
    private ExportImportInnerConverter exportImportConverter;

    @Autowired
    private LizardPluginApi pluginApi;

    @Autowired
    private ProjectConverter projectConverter;

    @Override
    public String getId() {
        return IMPORT_PROCESS;
    }

    @Override
    public void execute() {
        List<String> extensionsTitles = store.getIoPoints().keySet().stream().sorted().collect(Collectors.toList());
        if (extensionsTitles.isEmpty()) {
            uiUtils.createStageChild(stage -> {
                Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
                alertDialog.setHeaderText("The plugins for import have not been found!");
                Platform.runLater(alertDialog::showAndWait);
            });
            return;
        }

        List<String> exts = extensionsTitles.stream().map(s -> "*." + s).collect(Collectors.toList());
        uiUtils.showOpenDialogChooserFile("Import", exts.toString(), exts, this::handle);
    }

    private void handle(File file) {
        String ext = FileUtils.getExtensions(file.getName());
        ImportExportExtPoint importExportExtPoint = uiUtils.chooseImportExportExtPoint(ext);

        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ProcessViewModelImpl processViewModel = exportImportConverter.from(importExportExtPoint.read(pluginApi, file.getName(), bytes));

        store.changeCurrentProject(new ProjectModelImpl(
                ProjectUtils.generateProjectId(),
                projectConverter.createNewDescriptor(processViewModel),
                processViewModel)
        );
    }

}
