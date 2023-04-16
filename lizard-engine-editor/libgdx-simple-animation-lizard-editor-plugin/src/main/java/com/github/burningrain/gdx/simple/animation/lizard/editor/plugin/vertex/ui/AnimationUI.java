package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import javafx.scene.layout.VBox;

public abstract class AnimationUI extends VBox {

    public abstract void bindModel(SimpleAnimationVertexModel model);

    public abstract void unbindModel(SimpleAnimationVertexModel model);

}
