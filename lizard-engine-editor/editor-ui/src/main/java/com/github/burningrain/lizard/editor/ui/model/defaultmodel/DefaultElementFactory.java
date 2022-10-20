package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.ElementModelBinder;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;

public interface DefaultElementFactory {

    static PropertiesInspectorBinder<DefaultGraphElementModel, DefaultInspectorPaneController> createInspector() {
        return new DefaultPropertiesInspectorBinder();
    }

    static <T extends ElementModelBinder<DefaultGraphElementModel, DefaultGraphElementNode>> T createModelBinder() {
        return (T)new DefaultElementModelBinder();
    }

    static ElementDataConverter<DefaultGraphElementModel> createDataConverter() {
        return new DefaultElementDataConverter();
    }


}
