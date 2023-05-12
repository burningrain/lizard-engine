package com.github.burningrain.lizard.editor.api.project.model;

import com.github.burningrain.lizard.editor.api.project.model.GraphElementViewModel;
import com.github.burningrain.lizard.editor.api.project.model.GraphElementViewModelVisitor;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

public class VertexViewModel<D extends Serializable> extends GraphElementViewModel<D> {

    // графовые свойства
    private final SimpleFloatProperty x = new SimpleFloatProperty();
    private final SimpleFloatProperty y = new SimpleFloatProperty();
    private ObservableList<EdgeViewModel> edges = FXCollections.observableArrayList();

    // бизнес свойства. Плохо, что смешано...
    private final SimpleStringProperty className = new SimpleStringProperty();


    public float getX() {
        return x.get();
    }

    public SimpleFloatProperty xProperty() {
        return x;
    }

    public void setX(float x) {
        this.x.set(x);
    }

    public float getY() {
        return y.get();
    }

    public SimpleFloatProperty yProperty() {
        return y;
    }

    public void setY(float y) {
        this.y.set(y);
    }

    public ObservableList<EdgeViewModel> getEdges() {
        return edges;
    }

    public void setEdges(ObservableList<EdgeViewModel> edges) {
        this.edges = edges;
    }

    public String getClassName() {
        return className.get();
    }

    public SimpleStringProperty classNameProperty() {
        return className;
    }

    public void setClassName(String className) {
        this.className.set(className);
    }

    @Override
    public void accept(GraphElementViewModelVisitor visitor) {
        visitor.visit(this);
    }

}
