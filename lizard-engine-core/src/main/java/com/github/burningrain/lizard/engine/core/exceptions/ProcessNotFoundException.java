package com.github.burningrain.lizard.engine.core.exceptions;

public class ProcessNotFoundException extends LizardEngineUncheckedException {


    public ProcessNotFoundException(String processId) {
        super("processId=[" + processId + "]");
    }
}
