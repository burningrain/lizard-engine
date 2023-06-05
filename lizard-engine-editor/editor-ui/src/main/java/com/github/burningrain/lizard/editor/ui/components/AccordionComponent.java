package com.github.burningrain.lizard.editor.ui.components;

import com.github.burningrain.lizard.editor.api.project.model.descriptor.PluginDescriptor;
import com.github.burningrain.lizard.editor.ui.core.UiComponent;
import com.github.burningrain.lizard.editor.ui.draggers.VertexDragAndDrop;
import com.github.burningrain.lizard.editor.ui.model.ProcessElementsWrapper;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.collections.ObservableList;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Component
public class AccordionComponent implements UiComponent<Accordion> {

    @Autowired
    private Store store;

    @Autowired
    private VertexDragAndDrop vertexDragAndDrop;

    private final Accordion accordion = new Accordion();


    @Override
    public Accordion getNode() {
        return accordion;
    }

    @Override
    public void activate() {
        store.currentProjectModelProperty().addListener((observable, oldValue, newValue) -> {
            accordion.getPanes().clear();
            Collection<PluginDescriptor> pluginDescriptors = newValue.getDescriptor().getPluginDescriptors();
            for (PluginDescriptor pluginDescriptor : pluginDescriptors) {
                String pluginId = pluginDescriptor.getPluginId();
                ProcessElementsWrapper elementsWrapper = store.getProcessElements().get(pluginId);
                Objects.requireNonNull(elementsWrapper);
                TitledPaneComponent titledPaneComponent = new TitledPaneComponent(pluginId, vertexDragAndDrop, elementsWrapper.getVertexFactories().values());
                accordion.getPanes().add(titledPaneComponent.getTitledPane());
            }
            ObservableList<TitledPane> panes = accordion.getPanes();
            if(panes != null && !panes.isEmpty()) {
                accordion.setExpandedPane(panes.stream().findFirst().get());
            }
        });
    }

    @Override
    public void deactivate() {
        accordion.getPanes().clear();
    }

}
