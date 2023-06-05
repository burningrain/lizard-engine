package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.api.project.ProcessViewModel;
import com.github.burningrain.lizard.editor.api.project.ProjectDescriptor;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;

public class ProjectModelImpl implements ProjectModel {

    private final ProjectDescriptor descriptor;
    private final ProcessViewModel processViewModel;

    public ProjectModelImpl(ProjectDescriptor descriptor, ProcessViewModel processViewModel) {
        this.descriptor = descriptor;
        this.processViewModel = processViewModel;
    }

    public ProjectDescriptor getDescriptor() {
        return descriptor;
    }

    public ProcessViewModel getProcessViewModel() {
        return processViewModel;
    }

}
