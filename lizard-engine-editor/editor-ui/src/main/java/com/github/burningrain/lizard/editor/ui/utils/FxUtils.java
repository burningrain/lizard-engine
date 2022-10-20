package com.github.burningrain.lizard.editor.ui.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import com.github.burningrain.lizard.editor.ui.components.UiController;

import java.io.IOException;
import java.net.URL;

/**
 * Created by user on 03.02.2018.
 */
public class FxUtils {

//    public void runOnBackgroundThread(Task task) {
//        executorService.submit(task);
//    }
//
//    public void runOnBackgroundThread(Runnable runnable) {
//        VoidTask voidTask = new VoidTask() {
//            @Override
//            protected void callWithoutResult() {
//                runnable.run();
//            }
//        };
//        voidTask.setOnFailed(event -> {
//            throw new RuntimeException(Throwables.getStackTraceAsString(voidTask.getException()));
//        });
//        runOnBackgroundThread(voidTask);
//    }

    public void runOnFxThread(Task task) {
        Platform.runLater(task);
    }

    public static <T> T loadFxml(Object controller, String resourcePath) {
        FXMLLoader customFxmlLoader = new FXMLLoader();
        customFxmlLoader.setController(controller);
        URL location = FxUtils.class.getResource(resourcePath);
        customFxmlLoader.setLocation(location);

        try {
            return customFxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends UiController> T createUiController(T uiController, String fxmlPath) {
        Node node = loadFxml(uiController, fxmlPath);
        uiController.setNode(node);
        return uiController;
    }

    public static <T extends NodeContainer> T createUiNodeContainer(T nodeContainer, String fxmlPath) {
        loadFxml(nodeContainer, fxmlPath);
        return nodeContainer;
    }

}
