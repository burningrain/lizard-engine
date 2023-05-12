package com.github.burningrain.lizard.editor.ui.components;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.gvizfx.overview.GraphOverview;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.project.model.*;
import com.github.burningrain.lizard.editor.ui.components.controllers.*;
import com.github.burningrain.lizard.editor.ui.components.controllers.custom.CustomElementInspectorController;
import com.github.burningrain.lizard.editor.ui.components.controllers.custom.CustomProcessInspectorController;
import com.github.burningrain.lizard.editor.ui.core.UiComponent;
import com.github.burningrain.lizard.editor.ui.model.*;
import com.github.burningrain.lizard.editor.ui.utils.FxUtils;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.ref.WeakReference;

@Component
public class InspectorComponent implements UiComponent<Pane> {

    private final VBox pane = new VBox();

    @Autowired
    private Store store;

    @Autowired
    private LizardUiApi lizardUiApi;

    @Autowired
    private GraphView graphView;

    private InspectorVertexDefaultUiController vertexDefaultUiController;
    private InspectorEdgeDefaultUiController edgeDefaultUiController;

    private DefaultInspectorController<ProcessViewModelImpl<?, ?>> processController;

    private DefaultInspectorController defaultInspectorController;
    private UiController customInspectorController;

    private boolean isUiControllersBound = false;

    private final SetChangeListener<GraphElementViewModel> selectedGraphElementListener = new SetChangeListener<>() {

        private WeakReference<SetChangeListener.Change> prevChange;

        @Override
        public void onChanged(Change<? extends GraphElementViewModel> change) {
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


            if (isUiControllersBound) {
                pane.getChildren().remove(defaultInspectorController.unbind());

                Node node = customInspectorController.unbind();
                if (node != null) {
                    pane.getChildren().remove(node);
                }
            }

            ObservableSet<? extends GraphElementViewModel> set = change.getSet();
            if (set.size() == 0) {
                // элемент не выбран. возврат к основному отображению свойств самого процесса
                ProcessViewModelImpl processViewModel = (ProcessViewModelImpl)store.getCurrentProjectModel().getProcessViewModel();
                setInspectorPanel(
                        processController,
                        processViewModel,
                        createNewCustomInspector(true)
                        );
            } else if (set.size() == 1) {
                // выбран всего 1 элемент, показываем инспектор
                GraphElementViewModel selectedElementModel = set.iterator().next();
                final DefaultInspectorController[] controller = {null};
                selectedElementModel.accept(new GraphElementViewModelVisitor() {
                    @Override
                    public <D extends Serializable> void visit(VertexViewModel<D> vertexViewModel) {
                        controller[0] = vertexDefaultUiController;
                    }

                    @Override
                    public <D extends Serializable> void visit(EdgeViewModel<D> edgeViewModel) {
                        controller[0] = edgeDefaultUiController;
                    }
                });
                setInspectorPanel(
                        controller[0],
                        selectedElementModel,
                        createNewCustomInspector(false)
                );
            } else {
                //todo выбрано много элементов
            }
            isUiControllersBound = true;
        }
    };

    private <A extends LizardModel, B extends Serializable> void setInspectorPanel(
            DefaultInspectorController<A> defaultInspectorController,
            A inspectorModel,
            UiController customInspectorController
    ) {
        this.defaultInspectorController = defaultInspectorController;
        pane.getChildren().add(this.defaultInspectorController.bind(inspectorModel));

        this.customInspectorController = customInspectorController;
        Node node = this.customInspectorController.bind(inspectorModel);
        if (node != null) {
            pane.getChildren().add(node);
            VBox.setVgrow(node, Priority.ALWAYS);
        }
    }

    @Override
    public void activate() {
        pane.setSpacing(8);

        GraphOverview graphMap = graphView.getOverview();
        graphMap.fitWidthProperty().set(200);
        graphMap.fitHeightProperty().set(200);

        pane.getChildren().add(graphMap.getNode());

        store.currentProjectModelProperty().addListener((observable, oldValue, newValue) -> {
            SimpleSetProperty<GraphElementViewModel> selectedGraphElementsProperty = store.getCurrentProjectModel().getProcessViewModel().selectedGraphElementsProperty();
            selectedGraphElementsProperty.addListener(selectedGraphElementListener);
        });

        vertexDefaultUiController = createDefaultInspectorController(new InspectorVertexDefaultUiController(), InspectorVertexDefaultUiController.FXML_PATH);
        edgeDefaultUiController = createDefaultInspectorController(new InspectorEdgeDefaultUiController(), InspectorEdgeDefaultUiController.FXML_PATH);
        processController = createDefaultInspectorController(new DefaultProcessInspectorController(), DefaultProcessInspectorController.FXML_PATH);
    }

    private UiController createNewCustomInspector(boolean isProcessCustomController) {
        return isProcessCustomController ? new CustomProcessInspectorController(lizardUiApi, store) :
                new CustomElementInspectorController(lizardUiApi, store);
    }

    private <T extends DefaultInspectorController> T createDefaultInspectorController(T uiController, String fxmlPath) {
        return FxUtils.createUiController(uiController, fxmlPath);
    }

    @Override
    public void deactivate() {
        pane.getChildren().clear();
    }

    @Override
    public Pane getNode() {
        return pane;
    }

}
