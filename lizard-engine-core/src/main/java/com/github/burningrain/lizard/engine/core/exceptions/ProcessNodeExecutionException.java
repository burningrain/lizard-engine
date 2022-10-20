package com.github.burningrain.lizard.engine.core.exceptions;

import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.data.ProcessData;

public class ProcessNodeExecutionException extends LizardEngineUncheckedException {

    public ProcessNodeExecutionException(ProcessData processData, NodeData nodeData, Exception e) {
        super(createMessage(processData, nodeData, e.getMessage()), e);
    }

    private static String createMessage(ProcessData processData, NodeData nodeData, String exMsg) {
        StringBuilder builder = new StringBuilder();
        return builder.append(processData)
                .append("\n")
                .append(nodeData)
                .append("\n")
                .append("exception: ").append(exMsg).append("\n")

                .toString();
    }

}
