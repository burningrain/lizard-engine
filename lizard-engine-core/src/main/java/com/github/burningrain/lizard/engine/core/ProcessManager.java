package com.github.burningrain.lizard.engine.core;

import com.github.burningrain.lizard.engine.api.ProcessContext;
import com.github.burningrain.lizard.engine.api.nodes.NodeResult;
import com.github.burningrain.lizard.engine.core.exceptions.ProcessNotFoundException;

public class ProcessManager<PC extends ProcessContext, OUT> {

    private ProcessCache<PC, OUT> processCache;

    public ProcessManager(ProcessCache<PC, OUT> processCache) {
        this.processCache = processCache;
    }

    public NodeResult<OUT> process(PC context) {
        String processId = context.getProcessId();
        LizardProcess<PC, OUT> process = processCache.getProcess(processId);
        if(process == null) {
            throw new ProcessNotFoundException(processId);
        }
        return process.executeNextStep(context);
    }

}
