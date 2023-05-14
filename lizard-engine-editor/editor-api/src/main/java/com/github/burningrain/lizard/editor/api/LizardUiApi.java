package com.github.burningrain.lizard.editor.api;

import com.github.burningrain.gvizfx.GraphView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public interface LizardUiApi {

    GraphView getGraphView();

    void createStageChild(Consumer<Stage> consumer);

    <T extends Serializable> T getDataModelForCurrentProject(String key);

    void addOnCloseRequest(Consumer<? super WindowEvent> onCloseRequest);

    void showOpenDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer);

    void showSaveDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer);

    double getStageX();

    double getStageY();

}
