package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.AnimationModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class SimpleAnimationVertexModel implements AnimationModel {

    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleIntegerProperty from = new SimpleIntegerProperty();
    private final SimpleIntegerProperty to = new SimpleIntegerProperty();
    private final SimpleIntegerProperty frameFrequency = new SimpleIntegerProperty();
    private final SimpleObjectProperty<Animation.PlayMode> mode = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty looping = new SimpleBooleanProperty();

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getFrom() {
        return from.get();
    }

    public SimpleIntegerProperty fromProperty() {
        return from;
    }

    public void setFrom(int from) {
        this.from.set(from);
    }

    public int getTo() {
        return to.get();
    }

    public SimpleIntegerProperty toProperty() {
        return to;
    }

    public void setTo(int to) {
        this.to.set(to);
    }

    public int getFrameFrequency() {
        return frameFrequency.get();
    }

    public SimpleIntegerProperty frameFrequencyProperty() {
        return frameFrequency;
    }

    public void setFrameFrequency(int frameFrequency) {
        this.frameFrequency.set(frameFrequency);
    }

    public Animation.PlayMode getMode() {
        return mode.get();
    }

    public SimpleObjectProperty<Animation.PlayMode> modeProperty() {
        return mode;
    }

    public void setMode(Animation.PlayMode mode) {
        this.mode.set(mode);
    }

    public boolean isLooping() {
        return looping.get();
    }

    public SimpleBooleanProperty loopingProperty() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping.set(looping);
    }


}
