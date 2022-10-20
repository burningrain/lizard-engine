package com.github.burningrain.lizard.engine.core.exceptions;

public class LizardEngineUncheckedException extends RuntimeException {

    public LizardEngineUncheckedException() {
        super();
    }

    public LizardEngineUncheckedException(String message) {
        super(message);
    }

    public LizardEngineUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LizardEngineUncheckedException(Throwable cause) {
        super(cause);
    }

    protected LizardEngineUncheckedException(String message, Throwable cause,
                                             boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
