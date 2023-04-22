package com.github.burningrain.lizard.editor.api;

import com.github.burningrain.gvizfx.GraphView;

public interface ProjectLifecycleListener {

    // this method is called every time when you open the current project
    void handleOpenProjectEvent(GraphView graphView);

    void handleCloseProjectEvent(GraphView graphView);

}
