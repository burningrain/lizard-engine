package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class IntermediateStateUI extends AnimationUI {

    private final Rectangle rectangle = new Rectangle();
    private final Label label = new Label(Constants.INTERMEDIATE_STATE);

    public IntermediateStateUI() {
        rectangle.setHeight(40);
        rectangle.setWidth(80);
        rectangle.setFill(Color.LIGHTYELLOW);

        getChildren().addAll(label, rectangle);
    }

    @Override
    public void bindModel(SimpleAnimationVertexModel model) {
        label.textProperty().bindBidirectional(model.nameProperty());
    }

    @Override
    public void unbindModel(SimpleAnimationVertexModel model) {
        label.textProperty().unbindBidirectional(model.nameProperty());
    }

}
