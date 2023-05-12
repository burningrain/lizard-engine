package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.api.project.ProcessViewModel;
import com.github.burningrain.lizard.editor.api.project.ProjectDescriptor;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModelImpl;

public class ProjectModelImpl implements ProjectModel {

    private final ProjectDescriptorImpl descriptor;
    private final ProcessViewModelImpl processViewModel;

    public ProjectModelImpl(ProjectDescriptorImpl descriptor, ProcessViewModelImpl processViewModel) {
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
