package com.github.burningrain.gvizfx;

import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.property.GridProperty;
import com.github.burningrain.gvizfx.grid.*;
import com.github.burningrain.gvizfx.handlers.GraphViewBindersContainer;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.*;
import com.github.burningrain.gvizfx.handlers.binders.selecting.SelectingBoxBinder;
import com.github.burningrain.gvizfx.handlers.binders.selecting.SelectingElementBinder;
import com.github.burningrain.gvizfx.input.DefaultActions;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.layout.DefaultLayout;
import com.github.burningrain.gvizfx.layout.Layout;
import com.github.burningrain.gvizfx.model.*;
import com.github.burningrain.gvizfx.overview.GraphOverview;
import com.github.burningrain.gvizfx.overview.GraphOverviewCachedImpl;
import com.github.burningrain.gvizfx.overview.GraphOverviewHandlersBinder;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class GraphView extends ScrollPane {

    private final GraphViewProperty graphViewProperty;

    private final GraphViewModelImpl graphViewModel;

    private final Group group = new Group() {
        @Override
        protected void layoutChildren() {
            final double width = getWidth();
            final double height = getHeight();

            canvas.resizeRelocate(0, 0, width, height);
        }
    };
    private final Pane canvas;
    private final GraphViewData graphViewData;
    private final GraphViewHandlers graphViewHandlers;
    private final GraphViewBindersContainer bindingContainer;
    private final InputEventManager inputEventManager;

    private Layout layout;
    private GraphOverview graphOverview;

    private BackgroundGrid grid;
    private Class<? extends GraphHandlersBinder> graphOverviewHandlersBinderClass;

    public GraphView() {
        this(new GraphViewProperty(), new DotGrid());
    }

    public GraphView(GraphViewProperty graphViewProperty, BackgroundGrid grid) {
        this.graphViewProperty = Objects.requireNonNull(graphViewProperty);

        this.graphViewModel = new GraphViewModelImpl(graphViewProperty.elementModelProperty);
        this.layout = new DefaultLayout();

        this.canvas = new Pane();
        this.canvas.borderProperty().set(
                new Border(
                        new BorderStroke(Color.AQUA, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)
                )
        );

        this.graphViewData = createViewData();
        this.inputEventManager = new InputEventManager();
        // handlers
        this.graphViewHandlers = new GraphViewHandlers();
        this.bindingContainer = new GraphViewBindersContainer(graphViewData, graphViewHandlers, inputEventManager, graphViewProperty);

        this.setContent(group);
        if (grid != null) {
            setGrid(grid);
        }
        this.group.getChildren().add(canvas);

        registerBinders();
        registerInputEvents();
        bindModel();
        bindProperties();

        Platform.runLater(() -> {
            canvas.setMinWidth(this.getWidth() * 2);
            canvas.setMinHeight(this.getHeight() * 2);

            Platform.runLater(() -> {
                this.setHvalue(0.5);
                this.setVvalue(0.5);
            });
        });
    }

    public BackgroundGrid getGrid() {
        return grid;
    }

    // minimap
    public GraphOverview getOverview() {
        // lazy init
        if (graphOverview == null) {
            GraphOverviewCachedImpl graphOverview = new GraphOverviewCachedImpl();
            setOverview(graphOverview, new GraphOverviewHandlersBinder(graphOverview.getData()));
        }
        return graphOverview;
    }

    public GraphView setOverview(GraphOverview graphOverview, GraphHandlersBinder graphOverviewHandlersBinder) {
        Objects.requireNonNull(graphOverview);
        Objects.requireNonNull(graphOverviewHandlersBinder);
        if(this.graphOverview != null) {
            this.graphOverview.unbind();
            this.bindingContainer.unregisterBinder(this.graphOverviewHandlersBinderClass);
        }

        this.graphOverview = graphOverview;
        this.graphOverview.bind(graphViewData, graphViewProperty);
        this.bindingContainer.registerBinderAndApply(graphOverviewHandlersBinder);
        this.graphOverviewHandlersBinderClass = graphOverviewHandlersBinder.getClass();

        return this;
    }

    public Point2D translateToCanvasPoint(double x, double y) {
        Bounds layoutBounds = this.getContent().getLayoutBounds();
        double layoutWidth = layoutBounds.getWidth();
        double layoutHeight = layoutBounds.getHeight();

        Bounds viewportBounds = this.getViewportBounds();
        double viewportWidth = viewportBounds.getWidth();
        double viewportHeight = viewportBounds.getHeight();

        return new Point2D(
                x + layoutBounds.getMinX() + (layoutWidth - viewportWidth) * this.getHvalue(),
                y + layoutBounds.getMinY() + (layoutHeight - viewportHeight) * this.getVvalue()
        );
    }

    public GraphView setGrid(BackgroundGrid grid) {
        Objects.requireNonNull(grid);
        deregisterGrid();
        grid.init(this.graphViewProperty.gridProperty);
        registerGrid(grid, this.graphViewProperty.gridProperty);

        return this;
    }

    private void registerBinders() {
        bindingContainer.registerBinderAndApply(new VertexHandlersBinder());
        bindingContainer.registerBinderAndApply(new EdgeHandlersBinder());
        bindingContainer.registerBinderAndApply(new SelectingElementBinder());
        bindingContainer.registerBinderAndApply(new SelectingBoxBinder());
        bindingContainer.registerBinderAndApply(new DeleteGraphElementBinder());
        bindingContainer.registerBinderAndApply(new MovingCanvasBinder());

        bindingContainer.registerBinderAndApply(new FollowNodeStrategyBinder());
    }

    private void registerInputEvents() {
        //todo вероятно, имеет смысл по биндерам распределить?!
        inputEventManager.register(DefaultActions.BOX_SELECTION, DefaultActions.BOX_SELECTION_ACTION);
        inputEventManager.register(DefaultActions.MANY_ELEMENTS_SELECTION, DefaultActions.MANY_ELEMENTS_SELECTION_ACTION);
        inputEventManager.register(DefaultActions.DELETE_GRAPH_ELEMENT, DefaultActions.DELETE_ELEMENT_ACTION);
        inputEventManager.register(DefaultActions.MOVING_CANVAS, DefaultActions.MOVING_CANVAS_ACTION);
    }

    private void bindProperties() {
        graphViewProperty.gridProperty.isShowGrid.addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                deregisterGrid();
            } else {
                registerGrid(this.grid, this.graphViewProperty.gridProperty);
            }
        });
    }

    private void bindModel() {
        graphViewModel.verticesProperty().addListener((MapChangeListener<String, VertexElementWrapper>) change -> {
            if (change.wasRemoved()) {
                Node node = change.getValueRemoved().getElement();
                graphViewHandlers.getVertexHandlers().removeHandlers(node);
                canvas.getChildren().remove(node);
            }

            if (change.wasAdded()) {
                Collection<? extends VertexElementWrapper> values = change.getMap().values();
                Node node = change.getValueAdded().getElement();
                canvas.getChildren().add(node);
                graphViewHandlers.getVertexHandlers().applyHandlers(node);
                layout.lay(values.stream().map(GraphElementWrapper::getElement).collect(Collectors.toList()));
            }
        });
        graphViewModel.edgesProperty().addListener((MapChangeListener<String, EdgeElementWrapper>) change -> {
            if (change.wasRemoved()) {
                Node node = change.getValueRemoved().getElement();
                graphViewHandlers.getEdgeHandlers().removeHandlers(node);
                canvas.getChildren().remove(node);
            }

            if (change.wasAdded()) {
                Node node = change.getValueAdded().getElement();
                canvas.getChildren().add(node);
                graphViewHandlers.getEdgeHandlers().applyHandlers(node);
            }
        });
    }

    public GraphViewProperty getProperty() {
        return graphViewProperty;
    }

    public GraphViewBindersContainer getBindingsContainer() {
        return bindingContainer;
    }

    public InputEventManager getInputEventManager() {
        return inputEventManager;
    }

    public GraphViewModel getGraphViewModel() {
        return graphViewModel;
    }

    public void setLayout(Layout layout) {
        this.layout = Objects.requireNonNull(layout);
    }

    public Image snapshot() {
        return SnapshotUtils.snapshot(canvas);
    }

    private GraphViewData createViewData() {
        GraphViewData graphViewData = new GraphViewData();
        graphViewData.setGraphViewModel(graphViewModel);
        graphViewData.setScrollPane(this);
        graphViewData.setCanvas(canvas);
        graphViewData.setGraphView(this);
        graphViewData.setGrid(grid);

        return graphViewData;
    }

    private void registerGrid(BackgroundGrid grid, GridProperty gridProperty) {
        this.grid = grid;
        this.graphViewData.setGrid(this.grid);
        this.group.getChildren().add(0, this.grid.getNode());
        bindingContainer.registerBinderAndApply(new GridDrawBinder(gridProperty));
        Platform.runLater(() -> {
            GridUtils.relocateAndResizeGrid(graphViewData, gridProperty.gridSpacing.get());
        });
    }

    private void deregisterGrid() {
        if(this.grid != null) {
            this.group.getChildren().remove(this.grid.getNode());
            this.graphViewData.setGrid(null);
        }
        if (bindingContainer.isRegistered(GridDrawBinder.class)) {
            bindingContainer.unregisterBinder(GridDrawBinder.class);
        }
    }

}