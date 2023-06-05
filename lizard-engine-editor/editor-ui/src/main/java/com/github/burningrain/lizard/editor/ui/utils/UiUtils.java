package com.github.burningrain.lizard.editor.ui.utils;

import com.github.burningrain.gvizfx.handlers.ActionHandler;
import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;
import com.github.burningrain.lizard.editor.ui.model.IOWrapper;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.window.PluginIdChooserWindowController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class UiUtils {

    private Stage primaryStage;
    private final Store store;

    private final ActionHandler<WindowEvent> actionHandler = new ActionHandler<>();

    @Deprecated
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(actionHandler);
    }

    public UiUtils(Store store) {
        this.store = store;
    }

    public void createStageChild(Consumer<Stage> consumer) {
        consumer.accept(primaryStage);
    }

    public void showDialogChoosePluginExt(Collection<String> plugins, Consumer<String> consumer) {
        this.createStageChild(stage -> {
            Label label = new Label("Some plugins have been found. Please, choose one:");
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(plugins);

            VBox pane = new VBox();
            pane.getChildren().addAll(label, comboBox);

            Stage modalWindow = new Stage();
            modalWindow.setResizable(false);
            modalWindow.setTitle("Choosing a plugin");
            modalWindow.setScene(new Scene(pane));
            modalWindow.initModality(Modality.WINDOW_MODAL);
            modalWindow.initOwner(stage);
            Platform.runLater(() -> {
                modalWindow.showAndWait();
                consumer.accept(comboBox.getValue());
            });
        });
    }

    public void showDialogChoosePluginId(Store store, Consumer<String> consumer) {
        this.createStageChild(stage -> {
            Platform.runLater(() -> {
                PluginIdChooserWindowController controller = new PluginIdChooserWindowController();
                Pane rootNode = FxUtils.loadFxml(controller, PluginIdChooserWindowController.FXML_PATH);

                Stage modalWindow = new Stage();
                modalWindow.setResizable(false);
                modalWindow.setTitle("Choosing a plugin");
                modalWindow.setScene(new Scene(rootNode));
                modalWindow.initModality(Modality.WINDOW_MODAL);
                modalWindow.initOwner(stage);

                controller.init(
                        store,
                        "Choose a plugin:",
                        (String pluginId) -> {
                            modalWindow.close();
                            consumer.accept(pluginId);
                        },
                        event -> {
                            modalWindow.close();
                        }
                );
                modalWindow.show();
            });
        });
    }

    public ImportExportExtPoint chooseImportExportExtPoint(String extension) {
        IOWrapper ioWrapper = store.getIoPoints().get(extension);
        AtomicReference<ImportExportExtPoint> importExportExtPoint = new AtomicReference<>();
        if (ioWrapper.getSize() == 1) {
            importExportExtPoint.set(ioWrapper.getFirst());
        } else if (ioWrapper.getSize() != 0) {
            showDialogChoosePluginExt(ioWrapper.getPlugins(), pluginId -> {
                List<ImportExportExtPoint> points = ioWrapper.getPoints(pluginId);
                if (points.size() == 1) {
                    importExportExtPoint.set(points.get(0));
                } else {
                    //todo показать окно, выбрать конкретную имплементацию этого плагина
                }
            });
        }
        return importExportExtPoint.get();
    }

    public void showOpenDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer) {
        this.createStageChild(stage -> {
            FileChooser fileChooser = createFileChooser(title, desc, exts);
            Platform.runLater(() -> {
                File file = fileChooser.showOpenDialog(stage);
                //todo в другом потоке.
                if (file != null) {
                    consumer.accept(file);
                }
            });
        });
    }

    public void showSaveDialogChooserFile(String title, String desc, List<String> exts, Consumer<File> consumer) {
        this.createStageChild(stage -> {
            FileChooser fileChooser = createFileChooser(title, desc, exts);
            Platform.runLater(() -> {
                File file = fileChooser.showSaveDialog(stage);
                //todo в другом потоке.
                if (file != null) {
                    consumer.accept(file);
                }
            });
        });
    }

    private FileChooser createFileChooser(String title, String desc, List<String> exts) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(desc, exts);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        return fileChooser;
    }

    public void addOnCloseRequest(Consumer<? super WindowEvent> onCloseRequest) {
        actionHandler.addListener(onCloseRequest);
    }

    public double getStageX() {
        return primaryStage.getX();
    }

    public double getStageY() {
        return primaryStage.getY();
    }

}
