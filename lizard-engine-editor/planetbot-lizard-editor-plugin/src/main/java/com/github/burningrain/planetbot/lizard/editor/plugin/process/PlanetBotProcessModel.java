package com.github.burningrain.planetbot.lizard.editor.plugin.process;

import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

public class PlanetBotProcessModel implements Serializable {

    public final SimpleIntegerProperty maxAmountOfMoves = new SimpleIntegerProperty(200);

}
