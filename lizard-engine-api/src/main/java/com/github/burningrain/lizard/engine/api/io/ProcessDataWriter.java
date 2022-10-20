package com.github.burningrain.lizard.engine.api.io;

import com.github.burningrain.lizard.engine.api.data.ProcessData;

import java.util.List;

public interface ProcessDataWriter {

    void write(List<ProcessData> data);

}
