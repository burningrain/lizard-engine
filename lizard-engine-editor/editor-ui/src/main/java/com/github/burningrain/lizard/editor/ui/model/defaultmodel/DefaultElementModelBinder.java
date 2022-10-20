package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import com.github.burningrain.lizard.editor.api.EdgeModelBinder;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;

import java.util.Map;
import java.util.function.Consumer;

public class DefaultElementModelBinder implements
        VertexModelBinder<DefaultGraphElementModel, DefaultGraphElementNode>,
        EdgeModelBinder<DefaultGraphElementModel, DefaultGraphElementNode> {


    private SetChangeListener<String> showPropertiesListener;

    @Override
    public DefaultGraphElementNode createNode() {
        return new DefaultGraphElementNode();
    }

    @Override
    public Class<DefaultGraphElementNode> getNodeClass() {
        return DefaultGraphElementNode.class;
    }

    @Override
    public void bindNodeToModel(DefaultGraphElementNode node, DefaultGraphElementModel model) {
        showPropertiesListener = change -> {
            if(change.wasAdded() || change.wasRemoved()) {
                updateLabelText(node, change.getSet(), model.getProperties());
            }
        };
        ObservableSet<String> showPropertiesSet = model.getShowPropertiesSet();
        updateLabelText(node, showPropertiesSet, model.getProperties());
        showPropertiesSet.addListener(showPropertiesListener);
    }

    private void updateLabelText(DefaultGraphElementNode node, ObservableSet<? extends String> showPropertiesSet, Map<String, String> properties) {
        StringBuilder builder = new StringBuilder();
        showPropertiesSet.forEach((Consumer<String>) key -> {
            builder.append(key).append(": ").append(properties.get(key)).append("\n");
        });
        node.getLabel().setText(builder.toString());
    }

    @Override
    public void unbindNodeToModel(DefaultGraphElementNode node, DefaultGraphElementModel model) {
        model.getShowPropertiesSet().removeListener(showPropertiesListener);
    }

    @Override
    public DefaultGraphElementModel createNewNodeModel() {
        return new DefaultGraphElementModel();
    }

}
