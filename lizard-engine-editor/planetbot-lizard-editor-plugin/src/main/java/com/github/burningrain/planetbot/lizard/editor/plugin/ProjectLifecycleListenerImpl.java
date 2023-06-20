package com.github.burningrain.planetbot.lizard.editor.plugin;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.lizard.editor.api.ProjectLifecycleListener;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;
import javafx.scene.paint.Color;

public class ProjectLifecycleListenerImpl implements ProjectLifecycleListener {

    @Override
    public void handleOpenProjectEvent(GraphView graphView, ProjectModel currentProjectModel) {
        GraphViewProperty property = graphView.getProperty();
        property.elementModelProperty.edgeProperty.strokeWidth.set(1);
        property.elementModelProperty.edgeProperty.color.set(Color.BLACK);
    }

    @Override
    public void handleCloseProjectEvent(GraphView graphView, ProjectModel currentProjectModel) {

    }

}
