package com.github.burningrain.lizard.editor.api.ext;

import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import org.pf4j.ExtensionPoint;
import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.api.VertexFactory;

import java.util.List;

public interface ProcessElementsExtPoint extends ExtensionPoint {

    ProcessPropertiesInspectorBinder<?, ?> getProcessInspectorBinder();

    List<VertexFactory> getVertexFactoriesList();

    List<EdgeFactory> getEdgeFactoriesList();

}
