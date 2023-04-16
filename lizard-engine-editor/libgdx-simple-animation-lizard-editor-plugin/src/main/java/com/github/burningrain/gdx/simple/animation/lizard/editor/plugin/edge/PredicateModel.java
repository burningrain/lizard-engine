package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.AnimationModel;
import javafx.beans.property.SimpleStringProperty;

public class PredicateModel implements AnimationModel {

    private final SimpleStringProperty variableName = new SimpleStringProperty(this, "variableName");
    private final SimpleStringProperty operator = new SimpleStringProperty(this, "operator");
    private final SimpleStringProperty value = new SimpleStringProperty(this, "value");

    public String getVariableName() {
        return variableName.get();
    }

    public SimpleStringProperty variableNameProperty() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName.set(variableName);
    }

    public String getOperator() {
        return operator.get();
    }

    public SimpleStringProperty operatorProperty() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator.set(operator);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

}
