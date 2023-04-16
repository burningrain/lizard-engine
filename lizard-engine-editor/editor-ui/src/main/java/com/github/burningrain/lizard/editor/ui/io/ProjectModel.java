package com.github.burningrain.lizard.editor.ui.io;

import com.github.burningrain.lizard.editor.ui.io.descriptor.ProjectDescriptor;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModel;

public class ProjectModel {

    private ProjectDescriptor descriptor;
    private ProcessViewModel processViewModel;

    public ProjectModel(ProjectDescriptor descriptor, ProcessViewModel processViewModel) {
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
