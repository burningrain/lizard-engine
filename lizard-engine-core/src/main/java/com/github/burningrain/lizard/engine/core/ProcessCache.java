package com.github.burningrain.lizard.engine.core;

import com.github.burningrain.lizard.engine.api.ProcessContext;
import com.github.burningrain.lizard.engine.api.data.ProcessData;
import com.github.burningrain.lizard.engine.api.io.ProcessDataReader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessCache<PC extends ProcessContext, OUT> {

    private ProcessDataReader processDataReader;
    private ProcessDataConverter processDataConverter;
    private volatile Map<String, LizardProcess<PC, OUT>> processes;

    public ProcessCache(ProcessDataReader processDataReader, ProcessDataConverter processDataConverter) {
        this.processDataReader = processDataReader;
        this.processDataConverter = processDataConverter;

        update();
    }

    public void update() {
        HashMap<String, LizardProcess<PC, OUT>> map = new HashMap<>();

        List<ProcessData> processes = processDataReader.read();
        for (ProcessData process : processes) {
            map.put(process.getTitle(), createProcess(process));
        }
        this.processes = Collections.unmodifiableMap(map);
    }

    private LizardProcess createProcess(ProcessData sempProcess) {
        return new LizardProcess(sempProcess, processDataConverter.convert(sempProcess));
    }

    public LizardProcess<PC, OUT> getProcess(String processTitle) {
        return processes.get(processTitle);
    }

}
