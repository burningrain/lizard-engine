package com.github.burningrain.lizard.editor.api;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;

public interface ProjectLifecycleListener {

    // this method is called every time when you open the current project
    void handleOpenProjectEvent(GraphView graphView, ProjectModel currentProjectModel);

    void handleCloseProjectEvent(GraphView graphView, ProjectModel currentProjectModel); //TODO добавить вызов при смене вкладки проекта

}
