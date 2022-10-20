package com.github.burningrain.lizard.editor.ui.core.action;

import java.util.Stack;

public class CircularLifoBuffer<E> extends Stack<E> {

    private int maxSize;

    public CircularLifoBuffer(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public E push(E item) {
        super.push(item);
        while(this.size() > maxSize) {
            removeElementAt(size() - 1);
        }
        return item;
    }

}
