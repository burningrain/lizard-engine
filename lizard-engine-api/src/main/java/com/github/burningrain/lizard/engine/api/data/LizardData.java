package com.github.burningrain.lizard.engine.api.data;

import java.io.Serializable;

public abstract class LizardData implements Serializable {

    private final String title;
    private final String description;

    public LizardData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("title=[").append(title).append("]\n")
                .append("description=[").append(description).append("]\n")
        ;
        return builder.toString();
    }

}
