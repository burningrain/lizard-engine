package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge.SimpleAnimationEdgeFactory;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process.ProcessPropertiesInspectorBinderImpl;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.AnyStateVertexPropertiesInspectorBinder;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.VertexElementDataConverterImpl;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.VertexPropertiesInspectorBinder;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.binder.VertexModelBinderImpl;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.factory.AnyStateVertexFactory;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.factory.EndStateVertexFactory;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.factory.IntermediateEndStateVertexFactory;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.factory.StartStateVertexFactory;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui.AnyStateUI;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui.EndStateUI;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui.IntermediateStateUI;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.ui.StartStateUI;
import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.ProjectLifecycleListener;
import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.ext.ProcessElementsExtPoint;
import org.pf4j.Extension;

import java.util.Arrays;
import java.util.List;

@Extension
public class ProcessElementsExtPointImpl implements ProcessElementsExtPoint {

    @Override
    public ProcessPropertiesInspectorBinder<?, ?> getProcessInspectorBinder() {
        return new ProcessPropertiesInspectorBinderImpl();
    }

    @Override
    public List<VertexFactory> getVertexFactoriesList() {
        VertexElementDataConverterImpl dataConverter = new VertexElementDataConverterImpl();
        VertexPropertiesInspectorBinder inspectorBinder = new VertexPropertiesInspectorBinder();
        AnyStateVertexPropertiesInspectorBinder anyStateBinder = new AnyStateVertexPropertiesInspectorBinder();

        return Arrays.asList(
                new AnyStateVertexFactory(dataConverter, new VertexModelBinderImpl<>(AnyStateUI.class, AnyStateUI::new), anyStateBinder),
                new StartStateVertexFactory(dataConverter, new VertexModelBinderImpl<>(StartStateUI.class, StartStateUI::new), inspectorBinder),
                new IntermediateEndStateVertexFactory(dataConverter, new VertexModelBinderImpl<>(IntermediateStateUI.class, IntermediateStateUI::new), inspectorBinder),
                new EndStateVertexFactory(dataConverter, new VertexModelBinderImpl<>(EndStateUI.class, EndStateUI::new), inspectorBinder)
        );
    }

    @Override
    public List<EdgeFactory> getEdgeFactoriesList() {
        return Arrays.asList(new SimpleAnimationEdgeFactory());
    }

    @Override
    public ProjectLifecycleListener getProjectLifecycleListener() {
        return new ProjectLifecycleListenerImpl();
    }

}
