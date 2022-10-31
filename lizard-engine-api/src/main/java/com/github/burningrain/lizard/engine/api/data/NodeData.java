package com.github.burningrain.lizard.engine.api.data;

import java.util.Map;

public class NodeData extends ProcessElementData {

    private final String className;
    private final float x;
    private final float y;

    public NodeData(
            int id,
            String pluginId,
            String title,
            Map<String, String> attributes,
            float x,
            float y,
            String className,
            String description) {
        super(id, pluginId, title, description, attributes);
        this.className = className;
        this.x = x;
        this.y = y;
    }

    public String getClassName() {
        return className;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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
