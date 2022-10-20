package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.ui.utils.FxUtils;

public class DefaultPropertiesInspectorBinder implements PropertiesInspectorBinder<DefaultGraphElementModel, DefaultInspectorPaneController> {



    @Override
    public void bindInspector(DefaultGraphElementModel model, DefaultInspectorPaneController inspectorNode) {
        inspectorNode.bind(model);
    }

    @Override
    public void unbindInspector(DefaultGraphElementModel model, DefaultInspectorPaneController inspectorNode) {
        inspectorNode.unbind(model);
    }

    @Override
    public DefaultInspectorPaneController createPropertiesInspector() {
        return FxUtils.createUiNodeContainer(new DefaultInspectorPaneController(), "/fxml/default_prop_inspector.fxml");
    }

}
