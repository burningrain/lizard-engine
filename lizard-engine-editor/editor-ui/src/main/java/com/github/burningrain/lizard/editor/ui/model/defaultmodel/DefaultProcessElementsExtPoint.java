package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.ProjectLifecycleListener;
import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.ext.ProcessElementsExtPoint;
import com.github.burningrain.lizard.editor.ui.api.impl.EmptyProjectLifecycleListener;

import java.util.Collections;
import java.util.List;

public class DefaultProcessElementsExtPoint implements ProcessElementsExtPoint {

    @Override
    public ProcessPropertiesInspectorBinder<?, ?> getProcessInspectorBinder() {
        return new DefaultProcessPropertiesInspectorBinder();
    }

    @Override
    public List<VertexFactory> getVertexFactoriesList() {
        return Collections.singletonList(new DefaultVertexFactory());
    }

    @Override
    public List<EdgeFactory> getEdgeFactoriesList() {
        return Collections.singletonList(new DefaultEdgeFactory());
    }

    @Override
    public ProjectLifecycleListener getProjectLifecycleListener() {
        return EmptyProjectLifecycleListener.INSTANCE;
    }

}
