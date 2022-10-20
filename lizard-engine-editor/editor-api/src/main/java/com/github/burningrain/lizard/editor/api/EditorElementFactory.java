package com.github.burningrain.lizard.editor.api;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import com.github.burningrain.lizard.engine.api.data.LizardData;

import java.io.Serializable;

public interface EditorElementFactory<
        M extends Serializable,
        N extends Node,
        I extends NodeContainer,
        E extends ElementModelBinder<M, N>,
        D extends LizardData
        > {

    String getTitle();

    ImageView getImageView();

    PropertiesInspectorBinder<M, I> getElementInspectorBinder();

    E getElementModelBinder();

    ElementDataConverter<M> getElementDataConverter();

}