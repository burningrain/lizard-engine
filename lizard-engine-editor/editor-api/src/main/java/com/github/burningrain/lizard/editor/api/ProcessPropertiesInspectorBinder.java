package com.github.burningrain.lizard.editor.api;

import java.io.Serializable;

public interface ProcessPropertiesInspectorBinder<M extends Serializable, I extends NodeContainer>
        extends PropertiesInspectorBinder<M, I> {

    M createNewNodeModel();

    ElementDataConverter<M> getElementDataConverter();

}
