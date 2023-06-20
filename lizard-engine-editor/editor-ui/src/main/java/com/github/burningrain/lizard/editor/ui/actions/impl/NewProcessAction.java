package com.github.burningrain.lizard.editor.ui.actions.impl;


import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.project.model.descriptor.PluginDescriptor;
import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.io.ProjectConverter;
import com.github.burningrain.lizard.editor.ui.io.ProjectDescriptorImpl;
import com.github.burningrain.lizard.editor.ui.io.ProjectModelImpl;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModelImpl;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Scope("prototype")
@Component
public class NewProcessAction implements NotRevertAction {

    @Autowired
    private UiUtils uiUtils;

    @Autowired
    private Store store;

    @Autowired
    private ProjectConverter projectConverter;

    @Override
    public String getId() {
        return Actions.NEW_PROCESS;
    }

    @Override
    public void execute() {
        uiUtils.showDialogChoosePluginId(store, this::createProject);
    }

    private void createProject(String pluginId) {
        String title = UUID.randomUUID().toString();

        ProcessViewModelImpl processViewModel = new ProcessViewModelImpl();
        processViewModel.setProcessName(title);

        ProcessPropertiesInspectorBinder processBinder = store.getProcessPropertyBinders().get(pluginId);
        processViewModel.putDatum(pluginId, processBinder.createNewNodeModel());
        store.changeCurrentProject(new ProjectModelImpl(createDescriptor(title, pluginId), processViewModel));
    }

    private ProjectDescriptorImpl createDescriptor(String title, String pluginId) {
        List<PluginDescriptor> descriptorsList = projectConverter.createPluginDescriptorsList(Collections.singleton(pluginId), null);
        return projectConverter.createNewDescriptor(title, "", descriptorsList);
    }

}
