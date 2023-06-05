package com.github.burningrain.lizard.editor.ui.components.controllers.custom;

import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.Serializable;
import java.util.*;

public class CustomProcessInspectorController extends CustomInspectorController<Serializable> {

    public CustomProcessInspectorController(LizardUiApi lizardUiApi, Store store) {
        super(lizardUiApi, store);
    }

    @Override
    protected PropertiesInspectorBinder getBinder(Serializable model, Store store) {
        return new PropertiesInspectorBinderImpl(store);
    }

    private static class PropertiesInspectorBinderImpl implements PropertiesInspectorBinder<HashMap<String, Serializable>, NodeContainerImpl> {

        private final Store store;

        public PropertiesInspectorBinderImpl(Store store) {
            this.store = store;
        }

        @Override
        public void bindInspector(HashMap<String, Serializable> model, NodeContainerImpl inspectorNode) {
            inspectorNode.bindInspector(model);
        }

        @Override
        public void unbindInspector(HashMap<String, Serializable> model, NodeContainerImpl inspectorNode) {
            inspectorNode.unbindInspector(model);
        }

        @Override
        public NodeContainerImpl createPropertiesInspector() {
            return new NodeContainerImpl(store);
        }

    }

    private static class NodeContainerImpl implements NodeContainer {

        private final Map<String, NodeContainer> nodeMap;
        private final Store store;
        private final VBox vBox = new VBox();

        public NodeContainerImpl(Store store) {
            this.store = store;

            Set<String> pluginIds = store.getCurrentProjectPluginIds();
            if (pluginIds.isEmpty()) {
                this.nodeMap = Collections.emptyMap();
                return;
            }

            Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders = store.getProcessPropertyBinders();
            HashMap<String, NodeContainer> nodeMap = new HashMap<>();
            for (String pluginId : pluginIds) {
                ProcessPropertiesInspectorBinder inspectorBinder = processPropertyBinders.get(pluginId);
                NodeContainer propertiesInspector = inspectorBinder.createPropertiesInspector();
                nodeMap.put(pluginId, propertiesInspector);
                Node node = propertiesInspector.getNode();
                vBox.getChildren().add(node);
                vBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
                VBox.setVgrow(node, Priority.ALWAYS);
            }
            this.nodeMap = nodeMap;
        }

        public void bindInspector(HashMap<String, Serializable> model) {
            Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders = store.getProcessPropertyBinders();
            for (Map.Entry<String, Serializable> entry : model.entrySet()) {
                String pluginId = entry.getKey();
                processPropertyBinders.get(pluginId).bindInspector(entry.getValue(), nodeMap.get(pluginId));
            }
        }

        public void unbindInspector(HashMap<String, Serializable> model) {
            Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders = store.getProcessPropertyBinders();
            for (Map.Entry<String, Serializable> entry : model.entrySet()) {
                String pluginId = entry.getKey();
                processPropertyBinders.get(pluginId).unbindInspector(entry.getValue(), nodeMap.get(pluginId));
            }
        }

        @Override
        public Node getNode() {
            return vBox;
        }

        @Override
        public void init(LizardUiApi lizardUiApi) {
            for (NodeContainer nodeContainer : nodeMap.values()) {
                nodeContainer.init(lizardUiApi);
            }
        }

    }

}
