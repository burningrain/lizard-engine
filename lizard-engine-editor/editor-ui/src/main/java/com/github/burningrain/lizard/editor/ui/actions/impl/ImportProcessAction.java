package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.core.StageManager;
import com.github.burningrain.lizard.editor.ui.io.ExportImportInnerConverter;
import com.github.burningrain.lizard.editor.ui.io.ProjectModel;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModel;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.FileUtils;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;

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
                alertDialog.setHeaderText("Плагинов для импорта не обнаружено!");
                Platform.runLater(alertDialog::showAndWait);
            });
            return;
        }

        uiUtils.showOpenDialogChooserFile("Импорт процесса", "файл процесса", extensionsTitles, this::handle);
    }

    private void handle(File file) {
        String ext = FileUtils.getExtensions(file.getName());
        ImportExportExtPoint importExportExtPoint = uiUtils.chooseImportExportExtPoint(ext);

        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace(); //todo
        }
        ProcessViewModel processViewModel = exportImportConverter.from(importExportExtPoint.read(bytes));

        ProjectModel projectModel = new ProjectModel();
        //projectModel.setDescriptor(); todo
        projectModel.setProcessViewModel(processViewModel);
        store.setCurrentProjectModel(projectModel);
    }

}
