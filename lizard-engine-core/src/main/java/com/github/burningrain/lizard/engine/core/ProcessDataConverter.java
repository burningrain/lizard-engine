package com.github.burningrain.lizard.engine.core;

import com.github.burningrain.lizard.engine.api.ElementContext;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.data.ProcessData;
import com.github.burningrain.lizard.engine.api.data.TransitionData;
import com.github.burningrain.lizard.engine.api.elements.NodeElement;
import com.github.burningrain.lizard.engine.api.elements.TransitionTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessDataConverter implements Converter<ProcessData, Map<Integer, ProcessNode>> {

    private ElementContext elementContext;

    public ProcessDataConverter(ElementContext elementContext) {
        this.elementContext = elementContext;
    }

    @Override
    public Map<Integer, ProcessNode> convert(ProcessData processData) {
        return createProcessNodes(processData);
    }

    private Map<Integer, ProcessNode> createProcessNodes(ProcessData processData) {
        Map<Integer, NodeData> elements = toElementsMap(processData.getElements());
        Map<Integer, List<TransitionData>> transitions = toTransitionsMap(processData.getTransitions());

        Map<Integer, ProcessNode> result = new HashMap<>();
        bindNodes(processData.getStartElementId(), elements, transitions, result);
        return result;
    }

    private ProcessNode bindNodes(int parentId, Map<Integer, NodeData> elements,
                                  Map<Integer, List<TransitionData>> transitions, Map<Integer, ProcessNode> result) {
        NodeData nodeData = elements.get(parentId);
        NodeElement elementFromContext = getElementFromContext(nodeData.getClassName());

        HashMap<TransitionTag, ProcessNode> nextNodes = new HashMap<>();
        List<TransitionData> transitionData = transitions.get(parentId);
        for (TransitionData transitionDatum : transitionData) {
            int newParentId = transitionDatum.getTargetId();
            ProcessNode childNode = bindNodes(newParentId, elements, transitions, result);
            nextNodes.put(new TransitionTag(transitionDatum.getTag()), childNode);
        }

        ProcessNode processNode = new ProcessNode(nodeData, elementFromContext, nextNodes);
        result.put(parentId, processNode);
        return processNode;
    }

    private NodeElement getElementFromContext(String className) {
        try {
            return elementContext.getElement(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Integer, NodeData> toElementsMap(List<NodeData> elements) {
        HashMap<Integer, NodeData> result = new HashMap<>();
        for (NodeData element : elements) {
            result.put(element.getId(), element);
        }
        return result;
    }

    private static Map<Integer, List<TransitionData>> toTransitionsMap(List<TransitionData> transitions) {
        HashMap<Integer, List<TransitionData>> result = new HashMap<>();
        for (TransitionData transition : transitions) {
            List<TransitionData> list = result.get(transition.getSourceId());
            if (list == null) {
                list = new ArrayList<>();
                result.put(transition.getSourceId(), list);
            }
            list.add(transition);
        }
        return result;
    }

}
