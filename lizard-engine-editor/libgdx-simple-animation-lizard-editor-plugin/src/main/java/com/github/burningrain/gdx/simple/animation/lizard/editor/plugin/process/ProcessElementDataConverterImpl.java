package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.io.interpret.TypeEnum;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

import static com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.ImportExportExtPointImpl.JSON;

public class ProcessElementDataConverterImpl implements ElementDataConverter<SimpleAnimationProcessModel> {

    @Override
    public Map<String, String> exportNodeData(SimpleAnimationProcessModel model) {
        HashMap<String, String> result = new HashMap<>();
        result.put(Constants.VARIABLES, JSON.toJson(new VariablesDto(createVariablesMap(model.getVariables()))));
        result.put(Constants.START_STATE, model.getStartState());
        result.put(Constants.END_STATE, model.getEndState());
        // TODO добавить поддержку subfsm result.put(Constants.SUBFSMS, JSON.toJson(new SubFsmsDto(animationDto.getSubFsm())));

        return result;
    }

    @Override
    public SimpleAnimationProcessModel importNodeData(Map<String, String> data) {
        VariablesDto variablesDto = JSON.fromJson(VariablesDto.class, data.get(Constants.VARIABLES));

        SimpleAnimationProcessModel model = new SimpleAnimationProcessModel();
        model.setVariables(createVariablesList(variablesDto.getVariables()));
        model.setStartState(data.get(Constants.START_STATE));
        model.setEndState(data.get(Constants.END_STATE));
        // TODO добавить поддержку subfsm

        return model;
    }

    private ObjectMap<String, String> createVariablesMap(ObservableList<VariableModel> variables) {
        ObjectMap<String, String> result = new ObjectMap<>();
        for (VariableModel variable : variables) {
            result.put(variable.getName(), variable.getType());
        }

        return result;
    }

    private ObservableList<VariableModel> createVariablesList(ObjectMap<String, String> variables) {
        ObservableList<VariableModel> result = FXCollections.observableArrayList();
        for (ObjectMap.Entry<String, String> variable : variables) {
            VariableModel variableModel = new VariableModel();
            variableModel.setName(variable.key);
            variableModel.setType(TypeEnum.getByValue(variable.value).getValue());
            result.add(variableModel);
        }

        return result;
    }

}
