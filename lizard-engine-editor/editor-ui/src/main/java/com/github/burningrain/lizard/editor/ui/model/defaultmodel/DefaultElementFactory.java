package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.ElementModelBinder;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;

public interface DefaultElementFactory {

    static PropertiesInspectorBinder<DefaultGraphElementModel, DefaultInspectorPaneController> createInspector() {
        return new DefaultPropertiesInspectorBinder();
    }

    static <T extends DefaultElementModelBinder<DefaultGraphElementNode>> T createModelBinder(T modelBuilder) {
        return modelBuilder;
    }

    static ElementDataConverter<DefaultGraphElementModel> createDataConverter() {
        return new DefaultElementDataConverter();
    }


}
