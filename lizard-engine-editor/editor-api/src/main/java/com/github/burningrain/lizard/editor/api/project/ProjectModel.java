package com.github.burningrain.lizard.editor.api.project;

public interface ProjectModel {

    ProjectId getId();

    ProjectDescriptor getDescriptor();

    ProcessViewModel getProcessViewModel();

}
