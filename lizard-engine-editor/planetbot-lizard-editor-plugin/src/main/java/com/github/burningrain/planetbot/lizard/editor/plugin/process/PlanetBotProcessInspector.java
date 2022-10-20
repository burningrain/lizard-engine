package com.github.burningrain.planetbot.lizard.editor.plugin.process;

import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;


public class PlanetBotProcessInspector extends AnchorPane implements NodeContainer {

    private final TextField textField = new TextField();

    public PlanetBotProcessInspector() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("max moves: "), textField);
        hBox.setSpacing(4);

        getChildren().add(hBox);

        AnchorPane.setLeftAnchor(hBox, 8.0);
        AnchorPane.setRightAnchor(hBox, 8.0);
    }

    @Override
    public void init(LizardUiApi lizardUiApi) {

    }

    @Override
    public Node getNode() {
        return this;
    }

    public void bindModel(PlanetBotProcessModel model) {
        Bindings.bindBidirectional(textField.textProperty(), model.maxAmountOfMoves, new NumberStringConverter());
    }

    public void unbindModel(PlanetBotProcessModel model) {
        Bindings.unbindBidirectional(textField.textProperty(), model.maxAmountOfMoves);
    }

}
