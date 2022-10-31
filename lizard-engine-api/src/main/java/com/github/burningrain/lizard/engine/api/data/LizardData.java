package com.github.burningrain.lizard.engine.api.data;

import java.io.Serializable;
import java.util.Map;

public abstract class LizardData implements Serializable {

    private final String title;
    private final String description;
    private final Map<String, String> attributes;

    public LizardData(String title, String description, Map<String, String> attributes) {
        this.title = title;
        this.description = description;
        this.attributes = attributes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("title=[").append(title).append("]\n")
                .append("description=[").append(description).append("]\n")
                .append("attributes=[").append(attributes).append("]\n")
        ;
        return builder.toString();
    }

}
