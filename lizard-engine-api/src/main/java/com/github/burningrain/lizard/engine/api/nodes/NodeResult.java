package com.github.burningrain.lizard.engine.api.nodes;

import com.github.burningrain.lizard.engine.api.data.NodeData;

import java.util.List;

public class NodeResult<OUT> {

    private final OUT out;
    private final List<NodeData> elementData;

    public NodeResult(OUT out, List<NodeData> data) {
        this.out = out;
        this.elementData = data;
    }

    public OUT getOut() {
        return out;
    }

    public List<NodeData> getElementData() {
        return elementData;
    }

}
