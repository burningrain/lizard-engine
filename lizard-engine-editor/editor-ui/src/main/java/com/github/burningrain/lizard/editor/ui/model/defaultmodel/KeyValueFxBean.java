package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

import java.util.Objects;

public class KeyValueFxBean {

    private StringProperty key = new SimpleStringProperty(this, "key");
    private StringProperty value = new SimpleStringProperty(this, "value");
    private BooleanProperty show = new SimpleBooleanProperty(this, "show");

    private Listener listener;
    private ChangeListener<Boolean> showListener;
    private ChangeListener<String> keyListener;
    private ChangeListener<String> valueListener;

    public void removeListener() {
        listener = null;
        show.removeListener(showListener);
        key.removeListener(keyListener);
        value.removeListener(valueListener);
    }

    public KeyValueFxBean(String key, String value, boolean isShow, Listener listener) {
        this.key.set(key);
        this.value.set(value);
        this.show.set(isShow);
        this.listener = listener;

        this.show.addListener(showListener = (observable, oldValue, newValue) -> {
            listener.update(Listener.Type.SHOW_ON, this.key.get(), this.key.get(), this.value.get(), this.show.get());
        });
        this.key.addListener(keyListener = (observable, oldValue, newValue) -> {
            listener.update(Listener.Type.KEY, oldValue, newValue, this.value.get(), this.show.get());
        });
        this.value.addListener(valueListener = (observable, oldValue, newValue) -> {
            listener.update(Listener.Type.VALUE, this.key.get(), this.key.get(), newValue, this.show.get());
        });
    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public boolean isShow() {
        return show.get();
    }

    public BooleanProperty showProperty() {
        return show;
    }

    public void setShow(boolean show) {
        this.show.set(show);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValueFxBean that = (KeyValueFxBean) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public interface Listener {

        enum Type {
            KEY, VALUE, SHOW_ON
        }

        void update(Type type, String oldKeyValue, String newKeyValue, String value, boolean turnOn);
    }

}
