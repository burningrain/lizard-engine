package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;


public class FxUtils {

    public static <T> T loadFxml(T controller, String resourcePath) {
        FXMLLoader customFxmlLoader = new FXMLLoader();
        customFxmlLoader.setController(controller);
        URL location = FxUtils.class.getResource(resourcePath);
        customFxmlLoader.setLocation(location);

        try {
            customFxmlLoader.load();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
