package com.github.burningrain.lizard.engine.api.elements;

import java.util.Objects;

public class TransitionTag {

    private final String tag;

    public TransitionTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitionTag that = (TransitionTag) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag); //todo проверить такие места. Ядро и апишка должны быть понижены до jre 1.5
    }

}
