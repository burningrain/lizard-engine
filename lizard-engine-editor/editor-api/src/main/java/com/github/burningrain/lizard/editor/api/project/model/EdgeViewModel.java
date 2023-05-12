package com.github.burningrain.lizard.editor.api.project.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class EdgeViewModel<D extends Serializable> extends GraphElementViewModel<D> {

    private final SimpleBooleanProperty directional = new SimpleBooleanProperty(false);

    // графовые параметры
    private final SimpleIntegerProperty x1 = new SimpleIntegerProperty();
    private final SimpleIntegerProperty y1 = new SimpleIntegerProperty();
    private final SimpleIntegerProperty x2 = new SimpleIntegerProperty();
    private final SimpleIntegerProperty y2 = new SimpleIntegerProperty();
    private VertexViewModel vertexSource;
    private VertexViewModel vertexTarget;

    // бизнес параметры. Нехорошо смешивать...
    private final SimpleStringProperty tag = new SimpleStringProperty();


    public int getX1() {
        return x1.get();
    }

    public SimpleIntegerProperty x1Property() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1.set(x1);
    }

    public int getY1() {
        return y1.get();
    }

    public SimpleIntegerProperty y1Property() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1.set(y1);
    }

    public int getX2() {
        return x2.get();
    }

    public SimpleIntegerProperty x2Property() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2.set(x2);
    }

    public int getY2() {
        return y2.get();
    }

    public SimpleIntegerProperty y2Property() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2.set(y2);
    }

    public VertexViewModel getVertexSource() {
        return vertexSource;
    }

    public void setVertexSource(VertexViewModel vertexSource) {
        if(this.vertexSource != null) {
            x1.unbindBidirectional(this.vertexSource.xProperty());
            y1.unbindBidirectional(this.vertexSource.yProperty());
        }
        x1.bindBidirectional(vertexSource.xProperty());
        y1.bindBidirectional(vertexSource.yProperty());
        this.vertexSource = vertexSource;
    }

    public VertexViewModel getVertexTarget() {
        return vertexTarget;
    }

    public void setVertexTarget(VertexViewModel vertexTarget) {
        if(this.vertexTarget != null) {
            x2.unbindBidirectional(this.vertexTarget.xProperty());
            y2.unbindBidirectional(this.vertexTarget.yProperty());
        }
        x2.bindBidirectional(vertexTarget.xProperty());
        y2.bindBidirectional(vertexTarget.yProperty());
        this.vertexTarget = vertexTarget;
    }

    public String getTag() {
        return tag.get();
    }

    public SimpleStringProperty tagProperty() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public boolean isDirectional() {
        return directional.get();
    }

    public SimpleBooleanProperty directionalProperty() {
        return directional;
    }

    public void setDirectional(boolean directional) {
        this.directional.set(directional);
    }

    @Override
    public void accept(GraphElementViewModelVisitor visitor) {
        visitor.visit(this);
    }

}
