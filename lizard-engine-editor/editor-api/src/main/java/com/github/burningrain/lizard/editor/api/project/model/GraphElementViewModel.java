package com.github.burningrain.lizard.editor.api.project.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public abstract class GraphElementViewModel<D extends Serializable> implements LizardModel {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleObjectProperty<ProcessElementType> type = new SimpleObjectProperty<>();
    private final SimpleStringProperty description = new SimpleStringProperty();
    private final SimpleObjectProperty<D> data = new SimpleObjectProperty<>();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public D getData() {
        return data.get();
    }

    public SimpleObjectProperty<D> dataProperty() {
        return data;
    }

    public void setData(D data) {
        this.data.set(data);
    }

    public ProcessElementType getType() {
        return type.get();
    }

    public SimpleObjectProperty<ProcessElementType> typeProperty() {
        return type;
    }

    public void setType(ProcessElementType type) {
        this.type.set(type);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public abstract void accept(GraphElementViewModelVisitor visitor);

}
