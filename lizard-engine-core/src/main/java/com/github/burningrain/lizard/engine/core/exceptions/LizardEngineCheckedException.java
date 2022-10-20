package com.github.burningrain.lizard.engine.core.exceptions;

public class LizardEngineCheckedException extends Exception {

    public LizardEngineCheckedException() {
        super();
    }

    public LizardEngineCheckedException(String message) {
        super(message);
    }

    public LizardEngineCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LizardEngineCheckedException(Throwable cause) {
        super(cause);
    }

    protected LizardEngineCheckedException(String message, Throwable cause,
                                           boolean enableSuppression,
                                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
