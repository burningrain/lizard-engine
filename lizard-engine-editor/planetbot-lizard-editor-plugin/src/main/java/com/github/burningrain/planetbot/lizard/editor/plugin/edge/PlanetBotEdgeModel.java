package com.github.burningrain.planetbot.lizard.editor.plugin.edge;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class PlanetBotEdgeModel implements Serializable {

    private SimpleStringProperty scenarioType = new SimpleStringProperty();

    public String getScenarioType() {
        return scenarioType.get();
    }

    public SimpleStringProperty scenarioTypeProperty() {
        return scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType.set(scenarioType);
    }

}
