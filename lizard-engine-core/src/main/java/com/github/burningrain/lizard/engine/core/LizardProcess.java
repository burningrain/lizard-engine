package com.github.burningrain.lizard.engine.core;

import com.github.burningrain.lizard.engine.api.ProcessContext;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.data.ProcessData;
import com.github.burningrain.lizard.engine.api.elements.NodeElementResult;
import com.github.burningrain.lizard.engine.api.elements.TransitionTag;
import com.github.burningrain.lizard.engine.api.nodes.NodeResult;
import com.github.burningrain.lizard.engine.core.exceptions.NodeNotFoundException;
import com.github.burningrain.lizard.engine.core.exceptions.LizardProcessException;
import com.github.burningrain.lizard.engine.core.exceptions.ProcessNodeExecutionException;

import java.util.ArrayList;
import java.util.Map;

public class LizardProcess<PC extends ProcessContext, OUT> {

    private final ProcessData processData;
    private final Map<Integer, ProcessNode<PC, OUT>> steps;

    public LizardProcess(ProcessData processData, Map<Integer, ProcessNode<PC, OUT>> steps) {
        this.processData = processData;
        this.steps = steps;
    }

    public NodeResult<OUT> executeNextStep(PC context) {
        try {
            return execute(context);
        } catch (Exception e) {
            throw new LizardProcessException(processData, e);
        }
    }

    private NodeResult<OUT> execute(PC context) {
        int currentNodeId = context.getCurrentNodeId();
        ProcessNode<PC, OUT> processNode = steps.get(currentNodeId);
        if (processNode == null) {
            throw new NodeNotFoundException(currentNodeId);
        }

        ArrayList<NodeData> pathData = new ArrayList<>();
        ProcessNode<PC, OUT> nextNode = processNode;
        NodeElementResult<OUT> result = null;
        do {
            pathData.add(nextNode.getNodeData());
            try {
                result = nextNode.execute(context);
            } catch (Exception e) {
                throw new ProcessNodeExecutionException(processData, nextNode.getNodeData(), e);
            }
            TransitionTag tag = result.getTag();
            if (!result.isResult()) {
                nextNode = nextNode.getNextNode(tag);
            }
        } while (!result.isResult());

        return new NodeResult<>(result.getOut(), pathData);
    }

}
