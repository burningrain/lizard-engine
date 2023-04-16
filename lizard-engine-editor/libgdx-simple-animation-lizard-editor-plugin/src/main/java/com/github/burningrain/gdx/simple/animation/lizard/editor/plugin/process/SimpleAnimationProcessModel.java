package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.AnimationModel;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SimpleAnimationProcessModel implements AnimationModel {

    private final SimpleListProperty<VariableModel> variables = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final SimpleStringProperty startState = new SimpleStringProperty();
    private final SimpleStringProperty endState = new SimpleStringProperty();

    public ObservableList<VariableModel> getVariables() {
        return variables.get();
    }

    public SimpleListProperty<VariableModel> variablesProperty() {
        return variables;
    }

    public void setVariables(ObservableList<VariableModel> variables) {
        this.variables.set(variables);
    }

    public String getStartState() {
        return startState.get();
    }

    public SimpleStringProperty startStateProperty() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState.set(startState);
    }

    public String getEndState() {
        return endState.get();
    }

    public SimpleStringProperty endStateProperty() {
        return endState;
    }

    public void setEndState(String endState) {
        this.endState.set(endState);
    }

}
