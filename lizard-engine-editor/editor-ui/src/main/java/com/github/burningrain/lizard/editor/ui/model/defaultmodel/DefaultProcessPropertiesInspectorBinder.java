package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;

public class DefaultProcessPropertiesInspectorBinder extends DefaultPropertiesInspectorBinder implements ProcessPropertiesInspectorBinder<DefaultGraphElementModel, DefaultInspectorPaneController> {


    @Override
    public DefaultGraphElementModel createNewNodeModel() {
        return new DefaultGraphElementModel();
    }

    @Override
    public ElementDataConverter<DefaultGraphElementModel> getElementDataConverter() {
        return new DefaultElementDataConverter();
    }

}
