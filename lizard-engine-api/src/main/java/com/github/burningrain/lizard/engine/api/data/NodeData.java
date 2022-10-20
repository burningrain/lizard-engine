package com.github.burningrain.lizard.engine.api.data;

import java.util.Map;

public class NodeData extends ProcessElementData {

    private final String className;

    public NodeData(int id, String pluginId, String title, Map<String, String> attributes, String className, String description) {
        super(id, pluginId, title, description, attributes);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder
                .append("Node LizardData:").append("\n")
                .append(super.toString())
                .append("className=[").append(getClassName()).append("]\n")

                .toString();
    }

}
