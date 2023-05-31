package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.scene.image.ImageView;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;

public class DefaultVertexFactory implements VertexFactory<DefaultGraphElementModel, DefaultGraphElementNode, DefaultInspectorPaneController> {

    private final PropertiesInspectorBinder<DefaultGraphElementModel, DefaultInspectorPaneController> defaultPropertiesBinder = DefaultElementFactory.createInspector();
    private final VertexModelBinder<DefaultGraphElementModel, DefaultGraphElementNode> defaultElementModelBinder = DefaultElementFactory.createModelBinder(new DefaultVertexElementModelBinder());
    private final ElementDataConverter<DefaultGraphElementModel> defaultDataConverter = DefaultElementFactory.createDataConverter();

    @Override
    public String getTitle() {
        return "Unknown vertex";
    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public PropertiesInspectorBinder<DefaultGraphElementModel, DefaultInspectorPaneController> getElementInspectorBinder() {
        return defaultPropertiesBinder;
    }

    @Override
    public VertexModelBinder<DefaultGraphElementModel, DefaultGraphElementNode> getElementModelBinder() {
        return defaultElementModelBinder;
    }

    @Override
    public ElementDataConverter<DefaultGraphElementModel> getElementDataConverter() {
        return defaultDataConverter;
    }

}
