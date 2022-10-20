package com.github.burningrain.gvizfx.element;

import com.github.burningrain.gvizfx.property.SimpleGraphElementProperty;
import javafx.beans.property.Property;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.shape.Shape;

import java.util.Objects;

//todo нужно ли так глубоко пропихивать дженерик?
public abstract class GraphElement<N extends Node> extends ParentImpl implements GraphEventListener, NodeContainer<N> {

    private final String graphElementId;
    private final N node;

    private SimpleGraphElementProperty elementModel;

    public GraphElement(String graphElementId, N node) {
        this.graphElementId = Objects.requireNonNull(graphElementId);
        this.node = Objects.requireNonNull(node);
        this.getChildren().add(this.node);
    }

    public String getGraphElementId() {
        return graphElementId;
    }

    @Override
    public N getNode() {
        return node;
    }

    @Override
    public void setElementModel(SimpleGraphElementProperty elementModel) {
        this.elementModel = Objects.requireNonNull(elementModel);
    }

    @Override
    public SimpleGraphElementProperty getElementModel() {
        return elementModel;
    }

    //todo ифы...ифы...но пока так
    @Override
    public void select() {
        SimpleGraphElementProperty property = getElementModel();
        N userNode = getNode();
        if (userNode instanceof Region) {
            Region region = (Region) userNode;
            region.borderProperty().bind(property.selectionProperty.selectionBorder);
        } else if (userNode instanceof Shape) {
            Shape shape = (Shape) userNode;
            shape.strokeProperty().bind(property.selectionProperty.selectionColor);
        } else {
            //todo в логгер записать
        }
    }

    @Override
    public void deselect() {
        SimpleGraphElementProperty property = getElementModel();
        N userNode = getNode();
        if (userNode instanceof Region) {
            Region region = (Region) userNode;
            region.borderProperty().unbind();
            region.borderProperty().set(null);
        } else if (userNode instanceof Shape) {
            Shape shape = (Shape) userNode;
            shape.strokeProperty().bind(property.defaultColor);
        } else {
            //todo в логгер записать
        }
    }

    public <V, P extends Property<V> & WritableValue<V>> void setProperty(P property, V value) {
        if(property.isBound()) {
            property.unbind();
        }
        property.setValue(value);
    }

}
