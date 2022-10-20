package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.io.Serializable;
import java.util.HashSet;

public class DefaultGraphElementModel implements Serializable {

    private SimpleMapProperty<String, String> properties = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private SimpleSetProperty<String> showPropertiesSet = new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));

    public ObservableMap<String, String> getProperties() {
        return properties.get();
    }

    public SimpleMapProperty<String, String> propertiesProperty() {
        return properties;
    }

    public void setProperties(ObservableMap<String, String> properties) {
        this.properties.set(properties);
    }

    public ObservableSet<String> getShowPropertiesSet() {
        return showPropertiesSet.get();
    }

    public SimpleSetProperty<String> showPropertiesSetProperty() {
        return showPropertiesSet;
    }

    public void setShowPropertiesSet(ObservableSet<String> showPropertiesSet) {
        this.showPropertiesSet.set(showPropertiesSet);
    }

}
