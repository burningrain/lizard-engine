package com.github.burningrain.lizard.editor.ui.model;

import java.io.Serializable;

public interface GraphElementViewModelVisitor {


    <D extends Serializable> void visit(VertexViewModel<D> vertexViewModel);

    <D extends Serializable> void visit(EdgeViewModel<D> edgeViewModel);

}
