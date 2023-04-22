package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.scene.image.ImageView;
import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.api.EdgeModelBinder;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;

public class DefaultEdgeFactory implements EdgeFactory<DefaultGraphElementModel, DefaultGraphElementNode, DefaultInspectorPaneController> {

    @Override
    public String getTitle() {
        return "Unknown edge";
    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public PropertiesInspectorBinder<DefaultGraphElementModel, DefaultInspectorPaneController> getElementInspectorBinder() {
        return DefaultElementFactory.createInspector();
    }

    @Override
    public EdgeModelBinder<DefaultGraphElementModel, DefaultGraphElementNode> getElementModelBinder() {
        return DefaultElementFactory.createModelBinder();
    }

    @Override
    public ElementDataConverter<DefaultGraphElementModel> getElementDataConverter() {
        return DefaultElementFactory.createDataConverter();
    }

    @Override
    public boolean isEdgeDirectional() {
        return true;
    }
}
