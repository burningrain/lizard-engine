package com.github.burningrain.gvizfx.element;

import com.github.burningrain.gvizfx.property.EdgeProperty;
import com.github.burningrain.gvizfx.property.SimpleGraphElementProperty;
import com.github.burningrain.gvizfx.element.edge.ArrowEdgeElement;
import com.github.burningrain.gvizfx.element.edge.UserNodeEdgeElement;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class EdgeElement<
        E extends Node,
        A extends ArrowEdgeElement<?, E>> extends GraphElement<ParentImpl> {

    private final SimpleBooleanProperty isDirectional = new SimpleBooleanProperty();
    private final EdgeProperty edgeProperty = new EdgeProperty();

    private final UserNodeEdgeElement<?, E> userNodeElement;

    private final Supplier<A> arrowSupplier;
    private final E edge;

    private ChangeListener<Boolean> isDirectionalListener;
    private InvalidationListener recalculateElementListener;

    private A arrow;

    public EdgeElement(
            String graphElementId,
            boolean isDirectional,
            E edge,
            Supplier<A> arrowSupplier,
            UserNodeEdgeElement<?, E> userNodeElement
    ) {
        super(graphElementId, new ParentImpl());
        this.edge = Objects.requireNonNull(edge);
        this.arrowSupplier = Objects.requireNonNull(arrowSupplier);
        this.userNodeElement = userNodeElement;
        this.isDirectional.set(isDirectional);
    }

    protected E getEdge() {
        return edge;
    }

    protected abstract void setColor(E edge, Paint color);

    protected abstract void setStrokeWidth(E edge, double newValue);

    protected abstract void selectEdge(E edge, Paint color);

    protected abstract void deselectEdge(E edge, Paint color);

    protected abstract void recalculateElement(Node source, Node target, Scale scale);

    @Override
    public void setElementModel(SimpleGraphElementProperty elementModel) {
        super.setElementModel(elementModel);
        edgeProperty.bind(elementModel.edgeProperty);
    }

    public void bind(Node source, Node target, Scale scale) {
        bindEdge(source, target, scale);
        getNode().getChildren().add(edge);

        isDirectionalListener = (observable, oldValue, newValue) -> {
            if(newValue) {
                bindArrow(edge, source, target);
            } else {
                unbindArrow();
            }
        };
        isDirectionalProperty().addListener(isDirectionalListener);
        isDirectionalListener.changed(isDirectionalProperty(), isDirectionalProperty().get(), isDirectionalProperty().get());

        ChangeListener<Number> listener = (observable, oldValue, newValue) -> {
            setStrokeWidth(this.edge, newValue.doubleValue());
            if(arrow != null) {
                arrow.setStrokeWidth(newValue, getEdgeProperty().color.get());
            }
        };
        InvalidationListener paintListener = (observable) -> {
            Color color = getEdgeProperty().color.get();
            setColor(this.edge, color);
            if(arrow != null) {
                arrow.setColor(color);
            }
        };

        EdgeProperty edgeProperty = getEdgeProperty();

        SimpleDoubleProperty strokeWidth = edgeProperty.strokeWidth;
        strokeWidth.addListener(listener);
        listener.changed(strokeWidth, strokeWidth.get(), strokeWidth.get());

        SimpleObjectProperty<Color> color = edgeProperty.color;
        color.addListener(paintListener);
        paintListener.invalidated(color);

        if (userNodeElement != null) {
            bindUserNode(edge, source, target);
            getNode().getChildren().add(userNodeElement.getNode());
        }
    }

    public void unbind() {

    }

    @Override
    public void select() {
        Color color = getElementModel().selectionProperty.selectionColor.get();
        selectEdge(this.edge, color);
        if(isIsDirectional()) {
            arrow.setColor(color);
        }
        if (userNodeElement != null) {
            userNodeElement.setColor(color);
        }
    }

    @Override
    public void deselect() {
        Color color = getEdgeProperty().color.get();
        deselectEdge(this.edge, color);
        if(isIsDirectional()) {
            arrow.setColor(color);
        }
        if (userNodeElement != null) {
            userNodeElement.setColor(color);
        }
    }

    protected void bindEdge(Node source, Node target, Scale scale) {
        recalculateElementListener = observable -> {
            recalculateElement(source, target, scale);
        };

        source.layoutXProperty().addListener(recalculateElementListener);
        source.layoutYProperty().addListener(recalculateElementListener);
        target.layoutXProperty().addListener(recalculateElementListener);
        target.layoutYProperty().addListener(recalculateElementListener);

        recalculateElement(source, target, scale);
    }

    protected void bindArrow(E edgeNode, Node source, Node target) {
        arrow = arrowSupplier.get();
        arrow.bindElement(edgeNode, source, target);
        getNode().getChildren().add(arrow.getNode());
    }

    protected void unbindArrow() {
        if(arrow != null) {
            arrow.unbindElement();
            getNode().getChildren().remove(arrow.getNode());
            arrow = null;
        }
    }

    protected void bindUserNode(E edge, Node source, Node target) {
        userNodeElement.bindElement(edge, source, target);
    }

    public boolean isIsDirectional() {
        return isDirectional.get();
    }

    public SimpleBooleanProperty isDirectionalProperty() {
        return isDirectional;
    }

    public void setIsDirectional(boolean isDirectional) {
        this.isDirectional.set(isDirectional);
    }

    public EdgeProperty getEdgeProperty() {
        return edgeProperty;
    }

}