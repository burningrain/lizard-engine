package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge;


import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.AnimationModel;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class SimpleAnimationEdgeModel implements AnimationModel {

    private final SimpleStringProperty from = new SimpleStringProperty(this, "from");
    private final SimpleStringProperty to = new SimpleStringProperty(this, "to");
    private final SimpleListProperty<PredicateModel> fsmPredicates = new SimpleListProperty<>(this, "fsmPredicates");

    public String getFrom() {
        return from.get();
    }

    public SimpleStringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public String getTo() {
        return to.get();
    }

    public SimpleStringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public ObservableList<PredicateModel> getFsmPredicates() {
        return fsmPredicates.get();
    }

    public SimpleListProperty<PredicateModel> fsmPredicatesProperty() {
        return fsmPredicates;
    }

    public void setFsmPredicates(ObservableList<PredicateModel> fsmPredicates) {
        this.fsmPredicates.set(fsmPredicates);
    }

}
