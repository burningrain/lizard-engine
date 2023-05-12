package com.github.burningrain.lizard.editor.ui.components.controllers.custom;

import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.ui.components.UiController;
import com.github.burningrain.lizard.editor.api.project.model.LizardModel;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.scene.Node;

import java.io.Serializable;
import java.util.Objects;

public abstract class CustomInspectorController<M extends Serializable> implements UiController<M> {

    private final LizardUiApi lizardUiApi;
    private final Store store;
    private M graphElementViewModel;

    private NodeContainer nodeContainer;
    private PropertiesInspectorBinder inspectorBinder;

    public CustomInspectorController(LizardUiApi lizardUiApi, Store store) {
        this.lizardUiApi = lizardUiApi;
        this.store = store;
    }

    @Override
    public void setNode(Node node) {
        //todo кривота конечно все это, тянется от отсутствия привязки fx-ноды и контроллера
    }

    protected abstract PropertiesInspectorBinder getBinder(M graphElementViewModel, Store store);

    @Override
    public Node bind(M graphElementViewModel) {
        this.graphElementViewModel = graphElementViewModel;
        inspectorBinder = getBinder(graphElementViewModel, store);
        if (inspectorBinder == null) {
            return null;
        }

        nodeContainer = inspectorBinder.createPropertiesInspector();
        nodeContainer.init(lizardUiApi);
        Objects.requireNonNull(nodeContainer, "property inspector node must not be null. Inspector = " + inspectorBinder.getClass());
        if(graphElementViewModel instanceof LizardModel<?> lizardModel) {
            if(lizardModel.getData() != null) {
                inspectorBinder.bindInspector(lizardModel.getData(), nodeContainer);
            } else {
                //todo варнинг
            }
        }

        return Objects.requireNonNull(nodeContainer.getNode(), "Node from NodeContainer must not be null. NodeContainer = " + nodeContainer.getClass());
    }

    @Override
    public Node unbind() {
        if (nodeContainer == null) {
            return null;
        }

        if(graphElementViewModel instanceof LizardModel<?> lizardModel) {
            if(lizardModel.getData() != null) {
                inspectorBinder.unbindInspector(lizardModel.getData(), nodeContainer);
            }
        }

        return Objects.requireNonNull(nodeContainer.getNode(), "Node from NodeContainer must not be null. NodeContainer = " + nodeContainer.getClass());
    }

}
