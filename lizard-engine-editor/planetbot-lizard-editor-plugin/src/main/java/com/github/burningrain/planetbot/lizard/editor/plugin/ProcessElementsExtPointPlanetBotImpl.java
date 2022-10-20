package com.github.burningrain.planetbot.lizard.editor.plugin;

import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.planetbot.lizard.editor.plugin.process.PlanetBotProcessInspector;
import com.github.burningrain.planetbot.lizard.editor.plugin.process.PlanetBotProcessModel;
import com.github.burningrain.planetbot.lizard.editor.plugin.process.PropertiesInspectorBinderPlanetBotImpl;
import com.github.burningrain.planetbot.lizard.editor.plugin.vertex.PlanetBotVertexFactory;
import org.pf4j.Extension;
import com.github.burningrain.lizard.editor.api.EdgeFactory;
import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.ext.ProcessElementsExtPoint;
import com.github.burningrain.planetbot.lizard.editor.plugin.edge.PlanetBotEdgeFactory;

import java.util.Arrays;
import java.util.List;

@Extension
public class ProcessElementsExtPointPlanetBotImpl implements ProcessElementsExtPoint {

    @Override
    public ProcessPropertiesInspectorBinder<PlanetBotProcessModel, PlanetBotProcessInspector> getProcessInspectorBinder() {
        return new PropertiesInspectorBinderPlanetBotImpl();
    }

    @Override
    public List<VertexFactory> getVertexFactoriesList() {
        return Arrays.asList(new PlanetBotVertexFactory());
    }

    @Override
    public List<EdgeFactory> getEdgeFactoriesList() {
        return Arrays.asList(new PlanetBotEdgeFactory());
    }

}
