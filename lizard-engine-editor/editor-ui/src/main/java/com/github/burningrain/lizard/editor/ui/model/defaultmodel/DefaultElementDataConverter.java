package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.collections.FXCollections;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;

import java.util.Map;

public class DefaultElementDataConverter implements ElementDataConverter<DefaultGraphElementModel> {

    @Override
    public Map<String, String> exportNodeData(DefaultGraphElementModel model) {
        return model.getProperties();
    }

    @Override
    public DefaultGraphElementModel importNodeData(Map<String, String> data) {
        DefaultGraphElementModel model = new DefaultGraphElementModel();
        model.setProperties(FXCollections.observableMap(data));
        return model;
    }

}
