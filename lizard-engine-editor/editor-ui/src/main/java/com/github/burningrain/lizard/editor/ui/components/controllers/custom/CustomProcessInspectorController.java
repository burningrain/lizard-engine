package com.github.burningrain.lizard.editor.ui.components.controllers.custom;

import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModel;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CustomProcessInspectorController extends CustomInspectorController<Serializable> {

    public CustomProcessInspectorController(LizardUiApi lizardUiApi, Store store) {
        super(lizardUiApi, store);
    }

    @Override
    protected PropertiesInspectorBinder getBinder(Serializable model, Store store) {
        return new PropertiesInspectorBinderImpl(store.getProcessPropertyBinders());
    }

    private static class PropertiesInspectorBinderImpl implements PropertiesInspectorBinder<HashMap<String, Serializable>, NodeContainerImpl> {

        private final Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders;

        public PropertiesInspectorBinderImpl(Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders) {
            this.processPropertyBinders = processPropertyBinders;
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
            return new NodeContainerImpl(processPropertyBinders);
        }

    }

    private static class NodeContainerImpl extends AnchorPane implements NodeContainer {

        private final Map<String, NodeContainer> nodeMap;
        private final Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders;
        private final VBox vBox = new VBox();

        public NodeContainerImpl(Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders) {
            this.processPropertyBinders = processPropertyBinders;

            HashMap<String, NodeContainer> nodeMap = new HashMap<>();
            for (Map.Entry<String, ProcessPropertiesInspectorBinder> entry : processPropertyBinders.entrySet()) {
                NodeContainer propertiesInspector = entry.getValue().createPropertiesInspector();
                nodeMap.put(entry.getKey(), propertiesInspector);
                vBox.getChildren().add(propertiesInspector.getNode());
                vBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
            }
            this.nodeMap = nodeMap;

            this.getChildren().add(vBox);
        }

        public void bindInspector(HashMap<String, Serializable> model) {
            for (Map.Entry<String, Serializable> entry : model.entrySet()) {
                String pluginId = entry.getKey();
                processPropertyBinders.get(pluginId).bindInspector(entry.getValue(), nodeMap.get(pluginId));
            }
        }

        public void unbindInspector(HashMap<String, Serializable> model) {
            for (Map.Entry<String, Serializable> entry : model.entrySet()) {
                String pluginId = entry.getKey();
                processPropertyBinders.get(pluginId).unbindInspector(entry.getValue(), nodeMap.get(pluginId));
            }
        }

        @Override
        public Node getNode() {
            return this;
        }

        @Override
        public void init(LizardUiApi lizardUiApi) {
        }

    }

}
