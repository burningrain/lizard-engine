package com.github.burningrain.lizard.engine.core.exceptions;

public class NodeNotFoundException extends LizardEngineUncheckedException {

    public NodeNotFoundException(int currentStepNumber) {
        super("stepId=[" + currentStepNumber + "]");
    }

}
