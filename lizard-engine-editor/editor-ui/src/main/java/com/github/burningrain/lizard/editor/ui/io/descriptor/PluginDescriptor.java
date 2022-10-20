package com.github.burningrain.lizard.editor.ui.io.descriptor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PluginDescriptor implements Serializable {

    private String pluginId;
    private String pluginDescription;
    private String pluginClass;
    private String version;
    private String requires;
    private String provider;
    private List<PluginDependency> dependencies;
    private String license;
    private Collection<ElementType> elements = Collections.emptyList();

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginDescription() {
        return pluginDescription;
    }

    public void setPluginDescription(String pluginDescription) {
        this.pluginDescription = pluginDescription;
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequires() {
        return requires;
    }

    public void setRequires(String requires) {
        this.requires = requires;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public List<PluginDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<PluginDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Collection<ElementType> getElements() {
        return elements;
    }

    public void setElements(Collection<ElementType> elements) {
        this.elements = elements;
    }

}
