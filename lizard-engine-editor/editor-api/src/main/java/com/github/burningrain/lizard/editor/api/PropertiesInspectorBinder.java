package com.github.burningrain.lizard.editor.api;

import java.io.Serializable;

public interface PropertiesInspectorBinder<M extends Serializable, I extends NodeContainer> {

    void bindInspector(M model, I inspectorNode);

    void unbindInspector(M model, I inspectorNode);

    I createPropertiesInspector();

}
