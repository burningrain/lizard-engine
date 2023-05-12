package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin;

import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.preview.PreviewSingleton;
import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.lizard.editor.api.ProjectLifecycleListener;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;

public class ProjectLifecycleListenerImpl implements ProjectLifecycleListener {

    @Override
    public void handleOpenProjectEvent(GraphView graphView, ProjectModel currentProjectModel) {
        GraphViewProperty property = graphView.getProperty();
        property.elementModelProperty.edgeProperty.strokeWidth.set(2.5);
        //property.elementModelProperty.arrowProperty.length.set(30);
        property.elementModelProperty.edgeProperty.color.set(Color.WHEAT);
    }

    @Override
    public void handleCloseProjectEvent(GraphView graphView, ProjectModel currentProjectModel) {
        PreviewSingleton.getInstance().getConsumerOnWindowEvent().accept(new WindowEvent(null, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
