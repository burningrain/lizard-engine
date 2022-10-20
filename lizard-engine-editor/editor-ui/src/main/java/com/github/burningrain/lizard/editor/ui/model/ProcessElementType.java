package com.github.burningrain.lizard.editor.ui.model;

import java.io.Serializable;

public class ProcessElementType implements Serializable {

    private final String pluginId;
    private final String elementName;
    private final String description;

    private ProcessElementType(String pluginId, String elementName, String description) {
        this.pluginId = pluginId;
        this.elementName = elementName;
        this.description = description;
    }

    public static ProcessElementType of(String pluginId, String elementName) {
        return new ProcessElementType(pluginId, elementName, null);
    }

    public static ProcessElementType of(String pluginId, String elementName, String description) {
        return new ProcessElementType(pluginId, elementName, description);
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getElementName() {
        return elementName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessElementType that = (ProcessElementType) o;

        if (!pluginId.equals(that.pluginId)) return false;
        return elementName.equals(that.elementName);

    }

    @Override
    public int hashCode() {
        int result = pluginId.hashCode();
        result = 31 * result + elementName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "pluginId='" + pluginId + '\'' +
                "\nelementName='" + elementName;
    }

}
