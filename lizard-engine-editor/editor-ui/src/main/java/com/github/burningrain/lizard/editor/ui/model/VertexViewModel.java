package com.github.burningrain.lizard.editor.ui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

public class VertexViewModel<D extends Serializable> extends GraphElementViewModel<D> {

    // графовые свойства
    private final SimpleIntegerProperty x = new SimpleIntegerProperty();
    private final SimpleIntegerProperty y = new SimpleIntegerProperty();
    private ObservableList<EdgeViewModel> edges = FXCollections.observableArrayList();

    // бизнес свойства. Плохо, что смешано...
    private final SimpleStringProperty className = new SimpleStringProperty();


    public int getX() {
        return x.get();
    }

    public SimpleIntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public SimpleIntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
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
