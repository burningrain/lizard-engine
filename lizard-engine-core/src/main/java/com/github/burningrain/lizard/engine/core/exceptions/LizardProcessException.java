package com.github.burningrain.lizard.engine.core.exceptions;

import com.github.burningrain.lizard.engine.api.data.ProcessData;

public class LizardProcessException extends LizardEngineUncheckedException {

    public LizardProcessException(ProcessData processData, Exception e) {
        super(processData.toString(), e);
    }

}
