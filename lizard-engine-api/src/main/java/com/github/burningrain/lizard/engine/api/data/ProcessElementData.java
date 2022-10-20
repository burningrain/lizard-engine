package com.github.burningrain.lizard.engine.api.data;

import java.util.Map;

public class ProcessElementData extends LizardData {

    private final int id;
    private final String pluginId;
    private final Map<String, String> attributes;

    public ProcessElementData(int id, String pluginId, String title, String description, Map<String, String> attributes) {
        super(title, description);
        this.id = id;
        this.pluginId = pluginId;
        this.attributes = attributes;
    }

    public int getId() {
        return id;
    }

    public String getPluginId() {
        return pluginId;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("id=[").append(id).append("]\n")
                .append(super.toString())
                .append("pluginId=[").append(pluginId).append("]\n")
                .append("attributes=[").append(attributes).append("]\n")
        ;
        return builder.toString();
    }

}
