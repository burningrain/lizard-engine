package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.io.ExportImportInnerConverter;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.FileUtils;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;
import com.github.burningrain.lizard.engine.api.data.ProcessData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Scope("prototype")
@Component
public class ExportProcessAction implements NotRevertAction {

    @Autowired
    private UiUtils uiUtils;

    @Autowired
    private Store store;

    @Autowired
    private ExportImportInnerConverter exportImportConverter;

    @Override
    public String getId() {
        return Actions.EXPORT_PROCESS;
    }

    @Override
    public void execute() {
        if(store.getCurrentProjectModel() == null) return;

        List<String> extensionsTitles = store.getIoPoints().keySet().stream().sorted().collect(Collectors.toList());
        if(extensionsTitles.isEmpty()) {
            uiUtils.createStageChild(stage -> {
                Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
                alertDialog.setHeaderText("The plugins for export have not been found!");
                Platform.runLater(alertDialog::showAndWait);
            });
            return;
        }

        List<String> exts = extensionsTitles.stream().map(s -> "*." + s).collect(Collectors.toList());
        uiUtils.showSaveDialogChooserFile("Export", exts.toString(), exts, this::handle);
    }

    private void handle(File file) {
        String extension = FileUtils.getExtensions(file.getName());
        ImportExportExtPoint importExportPoint = uiUtils.chooseImportExportExtPoint(extension);
        ProcessData processData = exportImportConverter.to(store.getCurrentProjectModel().getProcessViewModel());
        try {
            Files.write(file.toPath(), importExportPoint.write(processData));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
