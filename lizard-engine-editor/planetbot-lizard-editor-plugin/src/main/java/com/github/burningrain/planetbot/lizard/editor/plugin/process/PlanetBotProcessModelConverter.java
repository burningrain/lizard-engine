package com.github.burningrain.planetbot.lizard.editor.plugin.process;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;

import java.util.Map;

public class PlanetBotProcessModelConverter implements ElementDataConverter<PlanetBotProcessModel>  {

    @Override
    public Map<String, String> exportNodeData(PlanetBotProcessModel model) {
        return Map.of(model.maxAmountOfMoves.getName(), String.valueOf(model.maxAmountOfMoves.get()));
    }

    @Override
    public PlanetBotProcessModel importNodeData(Map<String, String> data) {
        PlanetBotProcessModel model = new PlanetBotProcessModel();
        model.maxAmountOfMoves.set(Integer.parseInt(data.get(model.maxAmountOfMoves.getName())));
        return model;
    }

}
