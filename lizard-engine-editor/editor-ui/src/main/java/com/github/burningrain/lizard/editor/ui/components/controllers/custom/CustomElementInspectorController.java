package com.github.burningrain.lizard.editor.ui.components.controllers.custom;

import com.github.burningrain.lizard.editor.api.EditorElementFactory;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.PropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.ui.model.*;

import java.io.Serializable;

public class CustomElementInspectorController<N extends GraphElementViewModel> extends CustomInspectorController<N> {

    public CustomElementInspectorController(LizardUiApi lizardUiApi, Store store) {
        super(lizardUiApi, store);
    }

    @Override
    protected PropertiesInspectorBinder getBinder(N graphElementViewModel, Store store) {
        EditorElementFactory editorElementFactory = getElementFactory(graphElementViewModel, store);
        return editorElementFactory.getElementInspectorBinder();
    }

    private EditorElementFactory getElementFactory(N vertexViewModel, Store store) {
        final EditorElementFactory[] result = {null};
        vertexViewModel.accept(new GraphElementViewModelVisitor() {
            @Override
            public <D extends Serializable> void visit(VertexViewModel<D> vertexViewModel) {
                ProcessElementType type = vertexViewModel.getType();
                VertexFactoryWrapper vertexFactoryWrapper = store.getVertexFactoryWrapper(type);
                if (vertexFactoryWrapper == null) {
                    vertexFactoryWrapper = VertexFactoryWrapper.createDefault(type);
                }
                result[0] = vertexFactoryWrapper.getFactory();
            }

            @Override
            public <D extends Serializable> void visit(EdgeViewModel<D> edgeViewModel) {
                ProcessElementType type = edgeViewModel.getType();
                EdgeFactoryWrapper edgeFactoryWrapper = store.getEdgeFactoryWrapper(type);
                if (edgeFactoryWrapper == null) {
                    edgeFactoryWrapper = EdgeFactoryWrapper.createDefault(type);
                }
                result[0] = edgeFactoryWrapper.getFactory();
            }
        });
        if (result[0] == null) {
            throw new RuntimeException("Неизвестный тип элемента редактора процессов: " + vertexViewModel.getClass());
        }
        return result[0];
    }

}
