package com.github.burningrain.lizard.editor.ui.io.descriptor;

import java.io.Serializable;

public class PluginDependency implements Serializable {

    private String pluginId;
    private String version;

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
