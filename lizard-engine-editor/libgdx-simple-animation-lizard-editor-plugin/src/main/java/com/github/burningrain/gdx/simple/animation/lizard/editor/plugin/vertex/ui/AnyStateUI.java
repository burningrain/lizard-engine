package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AnyStateUI extends AnimationUI {

    private final Rectangle rectangle = new Rectangle();
    private final Label label = new Label(Constants.ANY_STATE);

    public AnyStateUI() {
        rectangle.setHeight(40);
        rectangle.setWidth(80);
        rectangle.setFill(Color.ORANGE);

        getChildren().addAll(label, rectangle);
    }

    @Override
    public void bindModel(SimpleAnimationVertexModel model) {
        model.nameProperty().bind(label.textProperty());
    }

    @Override
    public void unbindModel(SimpleAnimationVertexModel model) {
        model.nameProperty().unbind();
    }

}
