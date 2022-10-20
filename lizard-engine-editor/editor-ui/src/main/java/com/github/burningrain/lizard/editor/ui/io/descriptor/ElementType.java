package com.github.burningrain.lizard.editor.ui.io.descriptor;

import java.io.Serializable;

public class ElementType implements Serializable {

    private String pluginId;
    private String elementName;
    private String description;

    public ElementType() {
    }

    public ElementType(String pluginId, String elementName, String description) {
        this.pluginId = pluginId;
        this.elementName = elementName;
        this.description = description;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
