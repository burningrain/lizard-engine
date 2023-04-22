package com.github.burningrain.lizard.editor.ui.components.editor;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.gvizfx.model.*;
import com.github.burningrain.lizard.editor.api.*;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.ProcessEditorFsm;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.ProcessEditorFsmImpl;
import com.github.burningrain.lizard.editor.ui.components.editor.mode.States;
import com.github.burningrain.lizard.editor.ui.core.UiComponent;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.ui.draggers.VertexDragAndDrop;
import com.github.burningrain.lizard.editor.ui.io.ProjectModel;
import com.github.burningrain.lizard.editor.ui.model.*;
import com.github.burningrain.lizard.editor.ui.utils.FxUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class ProcessEditorComponent implements UiComponent<BorderPane> {

    @Autowired
    private VertexDragAndDrop vertexDragAndDrop;

    @Autowired
    private Store store;

    @Autowired
    private ActionManager actionManager;

    @Autowired
    private ActionFactory actionFactory;

    @Autowired
    private GraphView graphView;

    private BorderPane borderPane;

    private ToggleGroup modesBtnGroup;
    private ProcessEditorFsm processEditorFsm;

    public ProcessEditorComponent() {
        borderPane = new BorderPane();
    }

    private MapChangeListener vertexMapChangeListener = (MapChangeListener.Change change) -> {
        if (!(change.wasRemoved() || change.wasAdded())) {
            return;
        }

        GraphViewModel graphViewModel = graphView.getGraphViewModel();

        if (change.wasRemoved()) {
            VertexViewModel viewModel = (VertexViewModel) change.getValueRemoved();
            graphViewModel.verticesProperty().remove(viewModel.getId() + "");
        }

        if (change.wasAdded()) {
            VertexViewModel vertexViewModel = (VertexViewModel) change.getValueAdded();
            showVertex(graphViewModel, vertexViewModel);
        }
    };

    private MapChangeListener edgeMapChangeListener = (MapChangeListener.Change change) -> {
        if (!(change.wasRemoved() || change.wasAdded())) {
            return;
        }

        GraphViewModel graphViewModel = graphView.getGraphViewModel();

        if (change.wasRemoved()) {
            EdgeViewModel edgeViewModel = (EdgeViewModel) change.getValueRemoved();
            graphViewModel.edgesProperty().remove(edgeViewModel.getId() + ""); //FIXME кривота
        }

        if (change.wasAdded()) {
            EdgeViewModel edgeViewModel = (EdgeViewModel) change.getValueAdded();
            showEdge(graphViewModel, edgeViewModel);
        }
    };

    private SetChangeListener<GraphElementWrapper> selectedElementListener = new SetChangeListener<GraphElementWrapper>() {

        private WeakReference<Change> prevChange;

        @Override
        public void onChanged(Change<? extends GraphElementWrapper> change) {
            //fixme в биндерах в одном методе две операции (очитстка и добавление) воспринимаются как одна - последняя
            if (prevChange != null) {
                if (prevChange.get() == change) {
                    return;
                }
            }
            prevChange = new WeakReference<>(change);

            if (!(change.wasAdded() || change.wasRemoved())) {
                return;
            }

            GraphElementWrapper elementAdded = change.getElementAdded();
            if (elementAdded != null) {
                elementAdded.accept(new GraphElementWrapperVisitor() {
                    @Override
                    public void visit(VertexElementWrapper vertexElementWrapper) {
                        GraphElementViewModel elementViewModel = (VertexViewModel) store.getCurrentProjectModel().getProcessViewModel().getVertexes().get(vertexElementWrapper.getElementId());
                        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().add(elementViewModel);
                    }

                    @Override
                    public void visit(EdgeElementWrapper edgeElementWrapper) {
                        GraphElementViewModel elementViewModel = (EdgeViewModel) store.getCurrentProjectModel().getProcessViewModel().getEdges().get(edgeElementWrapper.getElementId());
                        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().add(elementViewModel);
                    }

                    @Override
                    public void visit(GraphElementWrapper graphElementWrapper) {
                        //todo создать перехватчик ошибок и логировать их, и выводить в окошке
                        throw new RuntimeException("НЕИЗВЕСТНЫЙ ТИП ЭЛЕМЕНТА [" + elementAdded + "]");
                    }
                });
            }

            GraphElementWrapper elementRemoved = change.getElementRemoved();
            if (elementRemoved != null) {
                elementRemoved.accept(new GraphElementWrapperVisitor() {
                    @Override
                    public void visit(VertexElementWrapper vertexElementWrapper) {
                        GraphElementViewModel elementViewModel = (VertexViewModel) store.getCurrentProjectModel().getProcessViewModel().getVertexes().get(vertexElementWrapper.getElementId());
                        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().remove(elementViewModel);
                    }

                    @Override
                    public void visit(EdgeElementWrapper edgeElementWrapper) {
                        GraphElementViewModel elementViewModel = (EdgeViewModel) store.getCurrentProjectModel().getProcessViewModel().getEdges().get(edgeElementWrapper.getElementId());
                        store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty().remove(elementViewModel);
                    }

                    @Override
                    public void visit(GraphElementWrapper graphElementWrapper) {
                        //todo создать перехватчик ошибок и логировать их, и выводить в окошке
                        throw new RuntimeException("НЕИЗВЕСТНЫЙ ТИП ЭЛЕМЕНТА [" + elementAdded + "]");
                    }
                });
            }
        }
    };

    private SetChangeListener<GraphElementViewModel> selectedGraphElementsProcessModelViewListener = new SetChangeListener<GraphElementViewModel>() {


        @Override
        public void onChanged(Change<? extends GraphElementViewModel> c) {
            if (!(c.wasAdded() || c.wasRemoved())) {
                return;
            }

            GraphViewModel graphViewModel = graphView.getGraphViewModel();
            graphViewModel.selectedElementsProperty().removeListener(selectedElementListener);

            if (c.wasAdded()) {
                GraphElementViewModel graphElementViewModel = c.getElementAdded();
                final GraphElementWrapper[] graphElementWrapper = {null};
                graphElementViewModel.accept(new GraphElementViewModelVisitor() {
                    @Override
                    public <D extends Serializable> void visit(VertexViewModel<D> vertexViewModel) {
                        graphElementWrapper[0] = graphViewModel.getVertices().get(vertexViewModel.getId() + "");
                    }

                    @Override
                    public <D extends Serializable> void visit(EdgeViewModel<D> edgeViewModel) {
                        graphElementWrapper[0] = graphViewModel.getEdges().get(edgeViewModel.getId() + "");
                    }
                });
                if (graphElementWrapper[0] != null) {
                    graphViewModel.selectedElementsProperty().add(graphElementWrapper[0]);
                }
            }

            if (c.wasRemoved()) {
                GraphElementViewModel graphElementViewModel = c.getElementRemoved();
                final GraphElementWrapper[] graphElementWrapper = {null};
                graphElementViewModel.accept(new GraphElementViewModelVisitor() {
                    @Override
                    public <D extends Serializable> void visit(VertexViewModel<D> vertexViewModel) {
                        graphElementWrapper[0] = graphViewModel.getVertices().get(vertexViewModel.getId() + "");
                    }

                    @Override
                    public <D extends Serializable> void visit(EdgeViewModel<D> edgeViewModel) {
                        graphElementWrapper[0] = graphViewModel.getEdges().get(edgeViewModel.getId() + "");
                    }
                });
                graphViewModel.selectedElementsProperty().remove(graphElementWrapper[0]);

            }

            graphViewModel.selectedElementsProperty().addListener(selectedElementListener);
        }
    };

    @Override
    public void activate() {
        GraphViewModel graphViewModel = graphView.getGraphViewModel();
        processEditorFsm = new ProcessEditorFsmImpl(graphView.getBindingsContainer(), actionManager, actionFactory, vertexDragAndDrop);

        store.currentProjectModelProperty().addListener(observable -> {
            graphViewModel.clear();
            linkListenersToCurrentProcess(graphViewModel);
            ProjectModel currentProjectModel = store.getCurrentProjectModel();
            //TODO сделать привязку конкретного плагина к конкретному виду фабрики ребер графа
            Optional<ProcessElementsWrapper> optionalFirst = store.getProcessElements().values().stream().findFirst();
            optionalFirst.ifPresent(processElementsWrapper -> {
                Optional<ProjectLifecycleListener> first = processElementsWrapper.getProjectListeners().values().stream().findFirst();
                first.ifPresent(projectLifecycleListener -> {
                    projectLifecycleListener.handleOpenProjectEvent(graphView);
                });
            });

            ProcessViewModel currentProcessViewModel = currentProjectModel.getProcessViewModel();
            currentProcessViewModel.getVertexes().values().forEach((Consumer<VertexViewModel>) vertexViewModel -> {
                showVertex(graphViewModel, vertexViewModel);
            });
            currentProcessViewModel.getEdges().values().forEach((Consumer<EdgeViewModel>) edgeViewModel -> {
                showEdge(graphViewModel, edgeViewModel);
            });
            //TODO сделать привязку конкретного плагина к конкретному виду фабрики ребер графа
            optionalFirst.ifPresent(processElementsWrapper -> {
                Optional<EdgeFactoryWrapper> first = processElementsWrapper.getEdgeFactories().values().stream().findFirst();
                currentProcessViewModel.selectedEdgeTypeProperty().set(first.get().getType());
            });

            borderPane.setTop(createInstrumentsHeader());
        });

        borderPane.setLeft(createModesPane());
        borderPane.setCenter(graphView);
    }

    private void linkListenersToCurrentProcess(GraphViewModel graphViewModel) {
        store.currentProjectModelProperty().get().getProcessViewModel().getVertexes().addListener(vertexMapChangeListener);
        store.currentProjectModelProperty().get().getProcessViewModel().getEdges().addListener(edgeMapChangeListener);
        store.currentProjectModelProperty().get().getProcessViewModel().getSelectedGraphElements().addListener(selectedGraphElementsProcessModelViewListener);
        graphViewModel.selectedElementsProperty().addListener(selectedElementListener);
    }

    //TODO
    private Pane createInstrumentsHeader() {
        HBox headerHBox = new HBox();

        return headerHBox;
    }

    private Pane createModesPane() {
        VBox modesPane = new VBox();
        modesBtnGroup = new ToggleGroup();

        ToggleButton selectBtn = createToggleButton(modesBtnGroup, "S", event -> {
            processEditorFsm.changeState(States.SELECT_GRAPH_ELEMENT);
        });
        ToggleButton createTransitionBtn = createToggleButton(modesBtnGroup, "T", event -> {
            processEditorFsm.changeState(States.CREATE_TRANSITION);
        });

        modesPane.getChildren().add(selectBtn);
        modesPane.getChildren().add(createTransitionBtn);

//        Platform.runLater(() -> {
//            double height = modesPane.getHeight();
//            modesPane.getTransforms().add(new Rotate(90, 0, height));
//            //todo а как поставить на место панельку из середины-то?
//            borderPane.setCenter(graphView);
//        });

        return modesPane;
    }

    private void showVertex(GraphViewModel graphViewModel, VertexViewModel vertexViewModel) {
        ProcessElementType type = vertexViewModel.getType();
        VertexFactoryWrapper vertexFactoryWrapper = store.getVertexFactoryWrapper(type);
        if (vertexFactoryWrapper == null) {
            vertexFactoryWrapper = VertexFactoryWrapper.createDefault(type);
        }
        VertexFactory vertexFactory = vertexFactoryWrapper.getFactory();
        VertexModelBinder modelBinder = (VertexModelBinder) vertexFactory.getElementModelBinder();

        //TODO отрефакторить на @NotNull
        Node newNode = null;
        if (modelBinder != null) {
            newNode = modelBinder.createNode();
            if (vertexViewModel.getData() != null) {
                modelBinder.bindNodeToModel(newNode, vertexViewModel.getData());
            } else {
                //todo заллогировать
            }
        }
        Node vertex = graphViewModel.addVertex(
                new VertexElementImpl(vertexViewModel, modelBinder, newNode),
                vertexViewModel.getX(),
                vertexViewModel.getY()
        );
        vertex.layoutXProperty().bindBidirectional(vertexViewModel.xProperty());
        vertex.layoutYProperty().bindBidirectional(vertexViewModel.yProperty());
    }

    private void showEdge(GraphViewModel graphViewModel, EdgeViewModel edgeViewModel) {
        ProcessElementType type = edgeViewModel.getType();
        EdgeFactoryWrapper edgeFactoryWrapper = store.getEdgeFactoryWrapper(type);
        if (edgeFactoryWrapper == null) {
            edgeFactoryWrapper = EdgeFactoryWrapper.createDefault(type);
        }
        EdgeFactory edgeFactory = edgeFactoryWrapper.getFactory();
        EdgeModelBinder edgeModelBinder = (EdgeModelBinder) edgeFactory.getElementModelBinder();

        Node edgeNode = null;
        if (edgeModelBinder != null) {
            edgeNode = edgeModelBinder.createNode();
            if (edgeViewModel.getData() != null) {
                edgeModelBinder.bindNodeToModel(edgeNode, edgeViewModel.getData());
            } else {
                //todo заллогировать
            }
        }

        graphViewModel.addEdge(
                edgeViewModel.getId() + "",
                edgeViewModel.isDirectional(),
                edgeViewModel.getVertexSource().getId() + "",
                edgeViewModel.getVertexTarget().getId() + "",
                createEdgeNode(edgeViewModel.tagProperty(), edgeNode)
        );
    }

    private Node createEdgeNode(SimpleStringProperty simpleStringProperty, Node edgeNode) {
        Label label = new Label();
        label.textProperty().bindBidirectional(simpleStringProperty);

        Pane result = new Pane();
        result.getChildren().add(label);
        if (edgeNode != null) {
            result.getChildren().add(edgeNode);
        }
        return result;
    }

    private static ToggleButton createToggleButton(ToggleGroup group, String title, EventHandler<ActionEvent> handler) {
        ToggleButton button = new ToggleButton(title);
        button.setOnAction(handler);
        button.setToggleGroup(group);
        return button;
    }

    @Override
    public void deactivate() {
        store.currentProjectModelProperty().set(null);
        borderPane.getChildren().clear();
        graphView = null;
        modesBtnGroup = null;
        processEditorFsm = null;
    }

    @Override
    public BorderPane getNode() {
        return borderPane;
    }

}
