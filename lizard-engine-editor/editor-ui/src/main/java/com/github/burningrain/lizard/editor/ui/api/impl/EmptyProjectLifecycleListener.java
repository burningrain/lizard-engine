package com.github.burningrain.lizard.editor.ui.api.impl;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.api.ProjectLifecycleListener;

public class EmptyProjectLifecycleListener implements ProjectLifecycleListener {

    public static final EmptyProjectLifecycleListener INSTANCE = new EmptyProjectLifecycleListener();

    @Override
    public void handleOpenProjectEvent(GraphView graphView) {

    }

    @Override
    public void handleCloseProjectEvent(GraphView graphView) {

    }

}
