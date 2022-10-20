package com.github.burningrain.lizard.engine.api.elements;

public class NodeElementResult<OUT> {

    private final boolean isResult;
    private final OUT out;

    private final TransitionTag tag;

    private NodeElementResult(boolean isResult, OUT out, TransitionTag tag) {
        this.isResult = isResult;
        this.out = out;
        this.tag = tag;
    }

    public static <OUT> NodeElementResult of(boolean isResult, OUT out) {
        return new NodeElementResult<>(isResult, out, null);
    }

    public static <OUT> NodeElementResult of(TransitionTag tag) {
        return new NodeElementResult<>(false, null, tag);
    }

    public boolean isResult() {
        return isResult;
    }

    public OUT getOut() {
        return out;
    }

    public TransitionTag getTag() {
        return tag;
    }

}
