package com.github.burningrain.gvizfx;

import com.github.burningrain.gvizfx.grid.BackgroundGrid;
import com.github.burningrain.gvizfx.model.GraphViewModel;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;

//todo ввести интерфейс, спрятать сеттеры
public class GraphViewData implements ViewData {

    // mandatory
    private GraphViewModel graphViewModel;
    private GraphView graphView;
    private Parent canvas;
    private ScrollPane scrollPane;
    private BackgroundGrid grid;

    public GraphViewModel getGraphViewModel() {
        return graphViewModel;
    }

    public void setGraphViewModel(GraphViewModel graphViewModel) {
        this.graphViewModel = graphViewModel;
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public void setGraphView(GraphView graphView) {
        this.graphView = graphView;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public Parent getCanvas() {
        return canvas;
    }

    public void setCanvas(Parent content) {
        this.canvas = content;
    }

    public BackgroundGrid getGrid() {
        return grid;
    }

    public void setGrid(BackgroundGrid grid) {
        this.grid = grid;
    }

}
