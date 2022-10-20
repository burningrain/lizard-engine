package com.github.burningrain.lizard.engine.api.data;

import java.util.Map;

public class TransitionData extends ProcessElementData {

    private final int sourceId;
    private final int targetId;
    private final String tag;

    public TransitionData(int id,
                          String pluginId,
                          String title,
                          String description,
                          Map<String, String> attributes,
                          int sourceId,
                          int targetId,
                          String tag) {
        super(id, pluginId, title, description, attributes);
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.tag = tag;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("Transition LizardData:").append("\n")
                .append(super.toString())
                .append("sourceId=[").append(sourceId).append("]\n")
                .append("targetId=[").append(targetId).append("]\n")
                .append("tag=[").append(tag).append("]\n")
        ;
        return builder.toString();
    }

}
