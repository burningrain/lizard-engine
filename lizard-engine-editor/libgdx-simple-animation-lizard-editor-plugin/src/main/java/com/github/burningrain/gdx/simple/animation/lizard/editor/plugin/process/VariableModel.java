package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.AnimationModel;
import javafx.beans.property.SimpleStringProperty;

public class VariableModel implements AnimationModel {

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty type = new SimpleStringProperty();

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

}
