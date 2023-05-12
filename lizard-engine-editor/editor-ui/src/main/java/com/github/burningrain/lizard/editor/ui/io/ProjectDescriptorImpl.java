package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.api.project.ProjectDescriptor;
import com.github.burningrain.lizard.editor.api.project.model.descriptor.PluginDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ProjectDescriptorImpl implements ProjectDescriptor {

    private String title;
    private String description;
    private String author;
    private String date;
    private Collection<PluginDescriptor> pluginDescriptors = Collections.emptyList();
    private Map<String, String> pluginsProcessData;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Collection<PluginDescriptor> getPluginDescriptors() {
        return pluginDescriptors;
    }

    public void setPluginDescriptors(Collection<PluginDescriptor> pluginDescriptors) {
        this.pluginDescriptors = pluginDescriptors;
    }

    public Map<String, String> getPluginsProcessData() {
        return pluginsProcessData;
    }

    public void setPluginsProcessData(Map<String, String> pluginsProcessData) {
        this.pluginsProcessData = pluginsProcessData;
    }

}
