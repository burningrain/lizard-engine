package com.github.burningrain.planetbot.lizard.editor.plugin.process;

import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import com.github.burningrain.planetbot.lizard.editor.plugin.io.Constants;

import java.util.HashMap;
import java.util.Map;

public class PlanetBotProcessModelConverter implements ElementDataConverter<PlanetBotProcessModel>  {

    @Override
    public Map<String, String> exportNodeData(PlanetBotProcessModel model) {
        HashMap<String, String> result = new HashMap<>();
        result.put(Constants.MAX_STEPS_COUNT, String.valueOf(model.maxAmountOfMoves.get()));

        return result;
    }

    @Override
    public PlanetBotProcessModel importNodeData(Map<String, String> data) {
        PlanetBotProcessModel model = new PlanetBotProcessModel();
        model.maxAmountOfMoves.set(Integer.parseInt(data.get(Constants.MAX_STEPS_COUNT)));

        return model;
    }

}
