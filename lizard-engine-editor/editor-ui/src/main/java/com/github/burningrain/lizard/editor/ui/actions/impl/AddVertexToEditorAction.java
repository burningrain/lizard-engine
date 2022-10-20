package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.model.VertexFactoryWrapper;
import com.github.burningrain.lizard.editor.ui.model.VertexViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;

import java.util.Random;

@Scope("prototype")
@Component
public class AddVertexToEditorAction implements RevertAction {

    @Autowired
    private Store store;

    private int x, y;
    private VertexFactoryWrapper vertexFactoryWrapper;

    private VertexViewModel vertexViewModel;

    public void setVertexFactoryWrapper(VertexFactoryWrapper vertexFactoryWrapper) {
        this.vertexFactoryWrapper = vertexFactoryWrapper;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String getId() {
        return Actions.ADD_VERTEX_TO_EDITOR_ACTION;
    }

    @Override
    public void execute() {
        VertexModelBinder elementModelBinder = (VertexModelBinder) vertexFactoryWrapper.getFactory().getElementModelBinder();

        vertexViewModel = new VertexViewModel();
        vertexViewModel.dataProperty().set(elementModelBinder.createNewNodeModel());
        vertexViewModel.setId(new Random().nextInt());
        vertexViewModel.setX(x);
        vertexViewModel.setY(y);
        vertexViewModel.setType(vertexFactoryWrapper.getType());
        vertexViewModel.setClassName(elementModelBinder.getNodeClass().getName());

        store.getCurrentProjectModel().getProcessViewModel().getVertexes().put(vertexViewModel.getId() + "", vertexViewModel);

        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().clear();
        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().add(vertexViewModel);
    }

    @Override
    public void revert() {
        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().remove(null);
        store.getCurrentProjectModel().getProcessViewModel().getVertexes().remove(vertexViewModel.getId() + "");
    }

}
