package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.api.project.ProcessViewModel;
import com.github.burningrain.lizard.editor.api.project.ProjectDescriptor;
import com.github.burningrain.lizard.editor.api.project.ProjectId;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;

import java.util.Objects;

public class ProjectModelImpl implements ProjectModel {

    private final ProjectId projectId;
    private final ProjectDescriptor descriptor;
    private final ProcessViewModel processViewModel;

    public ProjectModelImpl(ProjectId projectId, ProjectDescriptor descriptor, ProcessViewModel processViewModel) {
        this.projectId = Objects.requireNonNull(projectId);
        this.descriptor = Objects.requireNonNull(descriptor);
        this.processViewModel = Objects.requireNonNull(processViewModel);
    }

    @Override
    public ProjectId getId() {
        return projectId;
    }

    public ProjectDescriptor getDescriptor() {
        return descriptor;
    }

    public ProcessViewModel getProcessViewModel() {
        return processViewModel;
    }

}
