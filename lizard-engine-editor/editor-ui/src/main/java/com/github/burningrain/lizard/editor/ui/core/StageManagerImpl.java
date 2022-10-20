package com.github.burningrain.lizard.editor.ui.core;

import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

public abstract class StageManagerImpl implements StageManager {

    @Autowired(required = false)
    private MenuInitializer menuInitializer;

    @Autowired
    private HotKeysManager hotKeysManager;

    @Autowired
    private UiUtils uiUtils;

    private Scene scene;

    @PostConstruct
    public void init() {
        VBox vBox = new VBox();

        if(menuInitializer != null) {
            MenuBar menuBar = new MenuBar();
            menuBar.getMenus().addAll(menuInitializer.getMenus());
            vBox.getChildren().add(menuBar);
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setCenter(getMainNode());
        scene = new Scene(borderPane);
        //scene.setOnKeyPressed(hotKeysManager); TODO
        //scene.addEventFilter(EventType.ROOT, System.out::println);
    }

    @Override
    public void start(Stage primaryStage) {
        uiUtils.setPrimaryStage(primaryStage);
        start(primaryStage, scene);
    }

    protected abstract Node getMainNode();

    protected abstract void start(Stage primaryStage, Scene scene);

}
