package com.github.burningrain.lizard.engine.core;

import com.github.burningrain.lizard.engine.api.ProcessContext;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.elements.NodeElement;
import com.github.burningrain.lizard.engine.api.elements.NodeElementResult;
import com.github.burningrain.lizard.engine.api.elements.TransitionTag;

import java.util.Map;

public class ProcessNode<PC extends ProcessContext, OUT> {

    private final NodeData nodeData;
    private final NodeElement<PC, OUT> nodeElement;
    private final Map<TransitionTag, ProcessNode<PC, OUT>> nextNodes;

    public ProcessNode(
            NodeData nodeData,
            NodeElement<PC, OUT> nodeElement,
            Map<TransitionTag, ProcessNode<PC, OUT>> nextNodes) {
        this.nodeData = nodeData;
        this.nodeElement = nodeElement;
        this.nextNodes = nextNodes;
    }

    public NodeElementResult<OUT> execute(PC context) {
        return nodeElement.execute(context);
    }

    public ProcessNode<PC, OUT> getNextNode(TransitionTag tag) {
        return nextNodes.get(tag);
    }

    public NodeData getNodeData() {
        return nodeData;
    }

}
