package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.api.project.model.EdgeViewModel;
import com.github.burningrain.lizard.editor.api.project.model.GraphElementViewModel;
import com.github.burningrain.lizard.editor.api.project.model.GraphElementViewModelVisitor;
import com.github.burningrain.lizard.editor.api.project.model.VertexViewModel;
import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.io.ProjectModelImpl;
import com.github.burningrain.lizard.editor.ui.model.*;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Scope("prototype")
@Component
public class DeleteCurrentElementFromEditorAction implements RevertAction {

    private List<GraphElementViewModel> viewModelList;

    private ProjectModelImpl projectModel;

    @Autowired
    private Store store;

    @Override
    public String getId() {
        return Actions.DELETE_SELECTED_VERTEX_ACTION;
    }

    @Override
    public void execute() {
        projectModel = store.getCurrentProjectModel();
        ProcessViewModelImpl processViewModel = (ProcessViewModelImpl) projectModel.getProcessViewModel();
        this.viewModelList = new ArrayList<>(processViewModel.selectedGraphElementsProperty().get());
        if(viewModelList.isEmpty()) {
            //todo залогировать
            return;
        }

        for (GraphElementViewModel graphElementViewModel : viewModelList) {
            graphElementViewModel.accept(new GraphElementViewModelVisitor() {
                @Override
                public <D extends Serializable> void visit(VertexViewModel<D> graphElementViewModel) {
                    ObservableList<EdgeViewModel> edges = graphElementViewModel.getEdges();
                    for (EdgeViewModel edge : edges) {
                        processViewModel.getEdges().remove(edge.getId() + ""); //TODO разобраться с айдишниками. а то сопли какие-то
                    }

                    processViewModel.selectedGraphElementsProperty().remove(graphElementViewModel);
                    processViewModel.getVertexes().remove(graphElementViewModel.getId() + "");
                }

                @Override
                public <D extends Serializable> void visit(EdgeViewModel<D> edgeViewModel) {
                    edgeViewModel.getVertexSource().getEdges().remove(edgeViewModel);
                    edgeViewModel.getVertexTarget().getEdges().remove(edgeViewModel);

                    processViewModel.selectedGraphElementsProperty().remove(edgeViewModel);
                    processViewModel.getEdges().remove(edgeViewModel.getId() + "");
                }
            });
        }
    }

    @Override
    public void revert() {
        ProcessViewModelImpl processViewModel = (ProcessViewModelImpl) projectModel.getProcessViewModel();

        ArrayList<VertexViewModel> removedVertexes = new ArrayList<>();
        ArrayList<EdgeViewModel> removedEdges = new ArrayList<>();
        viewModelList.forEach(graphElementViewModel -> {
            graphElementViewModel.accept(new GraphElementViewModelVisitor() {
                @Override
                public <D extends Serializable> void visit(VertexViewModel<D> vertexViewModel) {
                    removedVertexes.add(vertexViewModel);
                }

                @Override
                public <D extends Serializable> void visit(EdgeViewModel<D> edgeViewModel) {
                    edgeViewModel.getVertexSource().getEdges().add(edgeViewModel);
                    edgeViewModel.getVertexTarget().getEdges().add(edgeViewModel);
                    removedEdges.add(edgeViewModel);
                }
            });
        });

        removedVertexes.forEach(vertexViewModel -> {
            ObservableMap<String, VertexViewModel> vertexes = processViewModel.getVertexes();
            vertexes.put(vertexViewModel.getId() + "", vertexViewModel);

            ObservableList<EdgeViewModel> edges = vertexViewModel.getEdges();
            for (EdgeViewModel edge : edges) {
                if(vertexes.containsKey(edge.getVertexSource().getId() + "") && vertexes.containsKey(edge.getVertexTarget().getId() + "")) {
                    processViewModel.getEdges().put(edge.getId() + "", edge);
                }
            }
            processViewModel.selectedGraphElementsProperty().add(vertexViewModel);
        });

        removedEdges.forEach(edgeViewModel -> {
            processViewModel.getEdges().put(edgeViewModel.getId() + "", edgeViewModel);
            processViewModel.selectedGraphElementsProperty().add(edgeViewModel);
        });
    }

}
