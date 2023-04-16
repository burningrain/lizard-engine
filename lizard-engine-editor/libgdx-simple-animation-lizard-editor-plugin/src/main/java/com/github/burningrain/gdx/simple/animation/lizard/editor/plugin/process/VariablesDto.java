package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process;

import com.badlogic.gdx.utils.ObjectMap;

import java.io.Serializable;

public class VariablesDto implements Serializable {

    private ObjectMap<String, String> variables;

    public VariablesDto(ObjectMap<String, String> variables) {
        this.variables = variables;
    }

    public VariablesDto() {
    }

    public ObjectMap<String, String> getVariables() {
        return variables;
    }

    public void setVariables(ObjectMap<String, String> variables) {
        this.variables = variables;
    }

}
