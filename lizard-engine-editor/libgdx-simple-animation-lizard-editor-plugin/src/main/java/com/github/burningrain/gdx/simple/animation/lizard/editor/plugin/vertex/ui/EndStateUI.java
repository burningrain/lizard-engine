package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class EndStateUI extends AnimationUI {

    private final Circle circle = new Circle();
    private final Label label = new Label(Constants.END_STATE);

    public EndStateUI() {
        int radius = 20;
        circle.setRadius(radius);
        circle.setFill(Color.SANDYBROWN);
        circle.setRadius(radius);
        circle.setCenterX(radius);
        circle.setCenterY(radius);

        getChildren().addAll(label, circle);
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
