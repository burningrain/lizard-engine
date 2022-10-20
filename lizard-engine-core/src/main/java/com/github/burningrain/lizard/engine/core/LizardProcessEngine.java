package com.github.burningrain.lizard.engine.core;

import com.github.burningrain.lizard.engine.api.ElementContext;
import com.github.burningrain.lizard.engine.api.ProcessContext;
import com.github.burningrain.lizard.engine.api.ProcessContextLoader;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.io.ProcessDataReader;
import com.github.burningrain.lizard.engine.api.io.ProcessDataWriter;
import com.github.burningrain.lizard.engine.api.nodes.NodeDataListener;
import com.github.burningrain.lizard.engine.api.nodes.NodeResult;
import com.github.burningrain.lizard.engine.core.exceptions.ProcessContextNotFoundException;

import java.util.List;

public class LizardProcessEngine<PC extends ProcessContext, IN, OUT> {

    private final ProcessManager<PC, OUT> processManager;
    private final ProcessCache<PC, OUT> processCache;
    private final ProcessDataConverter dataConverter;
    private final ProcessContextLoader<PC, IN> processContextLoader;

    private final NodeDataListener nodeDataListener;

    private LizardProcessEngine(Builder<PC, IN, OUT> builder) {
        nodeDataListener = builder.getNodeDataListener();
        processContextLoader = builder.getProcessContextLoader();
        dataConverter = new ProcessDataConverter(builder.getElementContext());
        processCache = new ProcessCache<>(builder.getProcessDataReader(), dataConverter);

        processManager = new ProcessManager<>(processCache);
    }

    public OUT process(IN in) {
        PC context = validateContext(processContextLoader.getContext(in));
        NodeResult<OUT> nodeResult = processManager.process(context);
        fireNodeData(nodeResult.getElementData());
        return nodeResult.getOut();
    }

    public void updateProcessesCache() {
        processCache.update();
    }

    private PC validateContext(PC context) {
        if (context == null) {
            throw new ProcessContextNotFoundException();
        }
        return context;
    }

    private void fireNodeData(List<NodeData> nodeData) {
        if (nodeDataListener != null) {
            nodeDataListener.update(nodeData);
        }
    }

    public static class Builder<PC extends ProcessContext, IN, OUT> {

        private ElementContext<PC, OUT> elementContext;
        private ProcessDataReader processDataReader;
        private ProcessDataWriter processDataWriter;
        private ProcessContextLoader<PC, IN> processContextLoader;
        private NodeDataListener nodeDataListener;

        public LizardProcessEngine build() {
            //todo проверки на null
            return new LizardProcessEngine<>(this);
        }

        public Builder<PC, IN, OUT> setElementContext(ElementContext<PC, OUT> elementContext) {
            this.elementContext = elementContext;
            return this;
        }

        public Builder<PC, IN, OUT> setProcessDataReader(ProcessDataReader processDataReader) {
            this.processDataReader = processDataReader;
            return this;
        }

        public Builder<PC, IN, OUT> setProcessDataWriter(ProcessDataWriter processDataWriter) {
            this.processDataWriter = processDataWriter;
            return this;
        }

        public Builder<PC, IN, OUT> setProcessContextLoader(ProcessContextLoader<PC, IN> processContextLoader) {
            this.processContextLoader = processContextLoader;
            return this;
        }

        public Builder<PC, IN, OUT> setNodeDataListener(NodeDataListener nodeDataListener) {
            this.nodeDataListener = nodeDataListener;
            return this;
        }

        ElementContext<PC, OUT> getElementContext() {
            return elementContext;
        }

        ProcessDataReader getProcessDataReader() {
            return processDataReader;
        }

        ProcessDataWriter getProcessDataWriter() {
            return processDataWriter;
        }

        ProcessContextLoader<PC, IN> getProcessContextLoader() {
            return processContextLoader;
        }

        NodeDataListener getNodeDataListener() {
            return nodeDataListener;
        }

    }

}
