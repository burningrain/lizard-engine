package com.github.burningrain.lizard.editor.api.project;

import com.github.burningrain.lizard.editor.api.project.model.descriptor.PluginDescriptor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ProjectDescriptor extends Serializable {

    String getTitle();

    String getDescription();

    String getAuthor();

    String getDate();

    Collection<PluginDescriptor> getPluginDescriptors();

    Map<String, String> getPluginsProcessData();


}
