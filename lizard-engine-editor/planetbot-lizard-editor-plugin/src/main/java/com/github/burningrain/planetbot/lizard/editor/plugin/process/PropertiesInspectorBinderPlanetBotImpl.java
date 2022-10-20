package com.github.burningrain.planetbot.lizard.editor.plugin.process;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;

public class PropertiesInspectorBinderPlanetBotImpl implements ProcessPropertiesInspectorBinder<PlanetBotProcessModel, PlanetBotProcessInspector> {

    @Override
    public void bindInspector(PlanetBotProcessModel model, PlanetBotProcessInspector inspectorNode) {
        inspectorNode.bindModel(model);
    }

    @Override
    public void unbindInspector(PlanetBotProcessModel model, PlanetBotProcessInspector inspectorNode) {
        inspectorNode.unbindModel(model);
    }

    @Override
    public PlanetBotProcessInspector createPropertiesInspector() {
        return new PlanetBotProcessInspector();
    }

    @Override
    public PlanetBotProcessModel createNewNodeModel() {
        return new PlanetBotProcessModel();
    }

    @Override
    public ElementDataConverter<PlanetBotProcessModel> getElementDataConverter() {
        return new PlanetBotProcessModelConverter();
    }

}
