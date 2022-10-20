package com.github.burningrain.planetbot.lizard.editor.plugin.vertex;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class PlanetBotVertexModel implements Serializable {

    public static final byte DEFAULT_RADIUS = 12;
    public static final Color DEFAULT_COLOR = Color.SANDYBROWN;

    private final SimpleObjectProperty<PlanetType> planetType = new SimpleObjectProperty<>(PlanetType.TYPE_A);
    private final SimpleBooleanProperty isBase = new SimpleBooleanProperty(false);

    private ChangeListener<PlanetType> planetTypeListener;
    private ChangeListener<Boolean> isBaseListener;

    private ChangeListener<PlanetType> selectedItemListener;

    //TODO реверсировать связь
    public void bindInspector(PlanetBotVertexFactory.PlanetBotVertexInspector inspectorNode) {
        ComboBox<PlanetType> planetTypeComboBox = (ComboBox<PlanetType>) inspectorNode.lookup("#planetTypeComboBox");
        planetTypeComboBox.getItems().addAll(PlanetType.values());
        selectedItemListener = (observable, oldValue, newValue) -> planetType.set(newValue);

        planetTypeComboBox.getSelectionModel().selectedItemProperty().addListener(selectedItemListener);
        planetTypeComboBox.getSelectionModel().select(planetType.get());

        CheckBox isMainCheckBox = (CheckBox) inspectorNode.lookup("#isMainCheckBox");
        isMainCheckBox.selectedProperty().bindBidirectional(isBase);
    }

    //TODO реверсировать связь
    public void unbindInspector(PlanetBotVertexFactory.PlanetBotVertexInspector inspectorNode) {
        ComboBox<PlanetType> planetTypeComboBox = (ComboBox<PlanetType>) inspectorNode.lookup("#planetTypeComboBox");
        planetTypeComboBox.getSelectionModel().selectedItemProperty().removeListener(selectedItemListener);

        CheckBox isMainCheckBox = (CheckBox) inspectorNode.lookup("#isMainCheckBox");
        isMainCheckBox.selectedProperty().unbindBidirectional(isBase);
    }

    //TODO реверсировать связь
    public void bindNode(Circle node) {
        planetTypeListener = (observable, oldValue, newValue) -> {
            double radius = DEFAULT_RADIUS * (1 + newValue.getCode());
            node.setRadius(radius);
            node.setCenterX(radius);
            node.setCenterY(radius);
        };
        isBaseListener = (observable, oldValue, newValue) -> {
            node.setFill(newValue ? Color.BROWN : DEFAULT_COLOR);
        };
        planetTypeProperty().addListener(planetTypeListener);
        isBaseProperty().addListener(isBaseListener);

        planetTypeListener.changed(planetTypeProperty(), planetTypeProperty().get(), planetTypeProperty().get());
        isBaseListener.changed(isBaseProperty(), isBaseProperty().get(), isBaseProperty().get());
    }

    public void unbindNode() {
        planetTypeProperty().removeListener(planetTypeListener);
        isBaseProperty().removeListener(isBaseListener);
    }


    public PlanetType getPlanetType() {
        return planetType.get();
    }

    public SimpleObjectProperty<PlanetType> planetTypeProperty() {
        return planetType;
    }

    public void setPlanetType(PlanetType planetType) {
        this.planetType.set(planetType);
    }

    public boolean isIsBase() {
        return isBase.get();
    }

    public SimpleBooleanProperty isBaseProperty() {
        return isBase;
    }

    public void setIsBase(boolean isBase) {
        this.isBase.set(isBase);
    }

}
