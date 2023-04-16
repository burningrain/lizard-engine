package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;

import java.util.HashMap;
import java.util.Map;

public class VertexElementDataConverterImpl implements ElementDataConverter<SimpleAnimationVertexModel> {

    @Override
    public Map<String, String> exportNodeData(SimpleAnimationVertexModel model) {
        HashMap<String, String> result = new HashMap<>();
        result.put(Constants.NAME, model.getName());

        if(Constants.ANY_STATE.equals(model.getName())) {
            // вообще этот иф - это косяк. У ANY_STATE должен быть свой набор характеристик, это уникальная нода
            return result;
        }

        result.put(Constants.FROM, String.valueOf(model.getFrom()));
        result.put(Constants.TO, String.valueOf(model.getTo()));
        result.put(Constants.FRAME_FREQUENCY, String.valueOf(model.getFrameFrequency()));
        result.put(Constants.PLAY_MODE, model.getMode().name());
        result.put(Constants.LOOPING, String.valueOf(model.isLooping()));

        return result;
    }

    @Override
    public SimpleAnimationVertexModel importNodeData(Map<String, String> data) {
        String name = data.get(Constants.NAME);
        if(Constants.ANY_STATE.equals(name)) {
            SimpleAnimationVertexModel model = new SimpleAnimationVertexModel();
            model.setName(name);
            return model;
        }

        SimpleAnimationVertexModel model = new SimpleAnimationVertexModel();
        model.setName(name);
        model.setFrom(Integer.parseInt(data.get(Constants.FROM)));
        model.setTo(Integer.parseInt(data.get(Constants.TO)));
        model.setFrameFrequency(Integer.parseInt(data.get(Constants.FRAME_FREQUENCY)));
        model.setMode(Animation.PlayMode.valueOf(data.get(Constants.PLAY_MODE)));
        model.setLooping(Boolean.parseBoolean(data.get(Constants.LOOPING)));

        return model;
    }

}
