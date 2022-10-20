package com.github.burningrain.lizard.engine.api.data;

import java.util.List;

public class ProcessData extends LizardData {

    private final List<NodeData> elements;
    private final List<TransitionData> transitions;

    //todo думаю, надо это убрать и отдать на откуп пользователю. А вот о чем оповещать - о том, что узел листовой
    private final int startElementId;

    public ProcessData(String title,
                       String description,
                       List<NodeData> elements,
                       List<TransitionData> transitions,
                       int startElementId
    ) {
        super(title, description);
        this.elements = elements;
        this.transitions = transitions;
        this.startElementId = startElementId;
    }

    public List<NodeData> getElements() {
        return elements;
    }

    public List<TransitionData> getTransitions() {
        return transitions;
    }

    public int getStartElementId() {
        return startElementId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder
                .append("Process LizardData:").append("\n")
                .append(super.toString())

                .toString();
    }

}
