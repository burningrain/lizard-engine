package com.github.burningrain.lizard.editor.ui.components;

import com.github.burningrain.lizard.editor.ui.actions.impl.LoadPluginDataToStoreAction;
import com.github.burningrain.lizard.editor.ui.core.UiComponent;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.ui.draggers.VertexDragAndDrop;
import com.github.burningrain.lizard.editor.ui.model.ProcessElementsWrapper;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.scene.control.Accordion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AccordionComponent implements UiComponent<Accordion> {

    @Autowired
    private Store store;

    @Autowired
    private ActionManager actionManager;

    @Autowired
    private ActionFactory actionFactory;

    @Autowired
    private VertexDragAndDrop vertexDragAndDrop;

    private Accordion accordion = new Accordion();


    @Override
    public Accordion getNode() {
        return accordion;
    }

    @Override
    public void activate() {
        actionManager.executeAction(actionFactory.createAction(LoadPluginDataToStoreAction.class)); //todo вынести отсюда куда-нить в инициализацию

        for (Map.Entry<String, ProcessElementsWrapper> entry : store.getProcessElements().entrySet()) {
            TitledPaneComponent titledPaneComponent = new TitledPaneComponent(entry.getKey(), vertexDragAndDrop, entry.getValue().getVertexFactories().values());
            accordion.getPanes().add(titledPaneComponent.getTitledPane());
        }
    }

    @Override
    public void deactivate() {
        accordion.getPanes().clear();
        accordion = null;
    }

}
