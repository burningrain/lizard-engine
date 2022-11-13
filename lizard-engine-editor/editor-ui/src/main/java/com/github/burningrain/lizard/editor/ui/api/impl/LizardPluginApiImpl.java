package com.github.burningrain.lizard.editor.ui.api.impl;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.api.LizardPluginApi;
import javafx.scene.image.Image;

public class LizardPluginApiImpl implements LizardPluginApi {

    private final GraphView graphView;

    public LizardPluginApiImpl(GraphView graphView) {
        this.graphView = graphView;
    }

    @Override
    public Image takeSnapshot() {
        return graphView.snapshot();
    }

    @Override
    public Image takeMinimapSnapshot() {
        return graphView.getOverview().snapshot();
    }

}
