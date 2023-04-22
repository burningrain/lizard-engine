package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.model.*;
import javafx.collections.ObservableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.github.burningrain.lizard.editor.api.EdgeModelBinder;

import java.util.Random;

@Scope("prototype")
@Component
public class AddEdgeToEditorAction implements RevertAction {

    @Autowired
    private Store store;

    private EdgeViewModel edgeViewModel;

    private String source;
    private String target;

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String getId() {
        return Actions.ADD_EDGE_TO_EDITOR_ACTION;
    }

    @Override
    public void execute() {
        ProcessElementType selectedEdgeType = store.getCurrentProjectModel().getProcessViewModel().getSelectedEdgeType();
        //todo сделать поддержку добавления нод и дуг в упрощенном режиме
        EdgeFactoryWrapper edgeFactoryWrapper = store.getProcessElements()
                .get(selectedEdgeType.getPluginId()).getEdgeFactories().get(selectedEdgeType.getElementName());
        EdgeFactory factory = edgeFactoryWrapper.getFactory();
        EdgeModelBinder elementModelBinder = (EdgeModelBinder) factory.getElementModelBinder();

        ObservableMap vertexes = store.getCurrentProjectModel().getProcessViewModel().getVertexes();
        VertexViewModel sourceViewModel = (VertexViewModel) vertexes.get(source);
        VertexViewModel targetViewModel = (VertexViewModel) vertexes.get(target);

        edgeViewModel = new EdgeViewModel();
        if(elementModelBinder != null) {
            edgeViewModel.dataProperty().set(elementModelBinder.createNewNodeModel());
        }
        edgeViewModel.setId(new Random().nextInt()); // todo а вот так не надо на самом деле. Нужен генератор идентификаторов
        edgeViewModel.setVertexSource(sourceViewModel);
        edgeViewModel.setVertexTarget(targetViewModel);
        edgeViewModel.setDirectional(factory.isEdgeDirectional());

        edgeViewModel.setType(edgeFactoryWrapper.getType());

        sourceViewModel.getEdges().add(edgeViewModel);
        targetViewModel.getEdges().add(edgeViewModel);

        store.getCurrentProjectModel().getProcessViewModel().getEdges().put(edgeViewModel.getId() + "", edgeViewModel);

        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().clear();
        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().add(edgeViewModel);
    }

    @Override
    public void revert() {
        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().remove(edgeViewModel);
        store.getCurrentProjectModel().getProcessViewModel().getEdges().remove(edgeViewModel.getId() + "");
    }

}