package com.github.burningrain.gvizfx.overview;

import com.github.burningrain.gvizfx.SnapshotUtils;
import com.github.burningrain.gvizfx.property.EdgeProperty;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.element.EdgeElement;
import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.element.VertexElement;
import com.github.burningrain.gvizfx.property.GraphOverviewProperty;
import com.github.burningrain.gvizfx.element.edge.EdgeFactory;
import com.github.burningrain.gvizfx.model.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class GraphOverviewCachedImpl extends Pane implements GraphOverview {

    private final Pane canvas = new Pane();
    private final Region rectangle = new Region();

    private final GraphOverviewData data = new GraphOverviewData(this, rectangle, canvas::getWidth, canvas::getHeight);

    private GraphViewData graphViewData;
    private GraphViewProperty graphViewProperty;
    private GraphOverviewProperty graphOverviewProperty;

    private final ObservableMap<String, Node> vertexesMap = FXCollections.observableHashMap();
    private final ObservableMap<String, Node> edgesMap = FXCollections.observableHashMap();

    private final Scale minimapScale = new Scale();

    private final MapChangeListener<String, VertexElementWrapper> vertexesListener = change -> {
        if (change.wasAdded()) {
            addVertexToMinimap(change.getValueAdded());
        } else if (change.wasRemoved()) {
            VertexElementWrapper valueRemoved = change.getValueRemoved();
            Node imageView = vertexesMap.remove(valueRemoved.getElement().getGraphElementId());

            canvas.getChildren().remove(imageView);
            imageView.layoutXProperty().unbind();
            imageView.layoutYProperty().unbind();
        }
    };

    private void addVertexToMinimap(VertexElementWrapper valueAdded) {
        VertexElement element = valueAdded.getElement();
        Node minimapNode = createVertex(element);
        vertexesMap.put(element.getGraphElementId(), minimapNode);
        canvas.getChildren().add(minimapNode);

        minimapNode.layoutXProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds bounds = graphViewData.getScrollPane().getContent().getLayoutBounds();
            double w = bounds.getWidth();
            double x = element.layoutXProperty().get() - bounds.getMinX();
            return x * canvas.widthProperty().get() / w;
        }, element.layoutXProperty(), canvas.widthProperty(), graphViewData.getScrollPane().getContent().layoutBoundsProperty()));
        minimapNode.layoutYProperty().bind(Bindings.createDoubleBinding(() -> {
            Bounds bounds = graphViewData.getScrollPane().getContent().getLayoutBounds();
            double h = bounds.getHeight();
            double y = element.layoutYProperty().get() - bounds.getMinY();
            return y * canvas.heightProperty().get() / h;
        }, element.layoutYProperty(), canvas.heightProperty(), graphViewData.getScrollPane().getContent().layoutBoundsProperty()));
    }

    private final MapChangeListener<String, EdgeElementWrapper> edgesListener = change -> {
        if (change.wasAdded()) {
            addEdgeToMinimap(change.getValueAdded());
        } else if (change.wasRemoved()) {
            EdgeElementWrapper valueRemoved = change.getValueRemoved();
            Node edge = edgesMap.remove(valueRemoved.getElement().getGraphElementId());
            canvas.getChildren().remove(edge);
        }
    };

    private void addEdgeToMinimap(EdgeElementWrapper valueAdded) {
        EdgeElement element = valueAdded.getElement();

        Node edge = createEdge(element);
        edgesMap.put(element.getGraphElementId(), edge);
        canvas.getChildren().add(edge);
    }

    private Node createEdge(EdgeElement element) {
        GraphViewModel graphViewModel = this.graphViewData.getGraphViewModel();

        EdgeElementWrapper edgeElementWrapper = graphViewModel.getEdges().get(element.getGraphElementId());
        VertexElementWrapper source = edgeElementWrapper.getSource();
        VertexElementWrapper target = edgeElementWrapper.getTarget();
        boolean isSelf = source.getElementId().equals(target.getElementId());

        EdgeFactory<? extends EdgeElement> edgeFactory = graphViewModel.getEdgeFactory();
        EdgeElement overviewEdge = edgeFactory.createEdge(element.getGraphElementId(), false, isSelf, null);
        overviewEdge.setElementModel(graphViewProperty.elementModelProperty);
        overviewEdge.bind(vertexesMap.get(source.getElementId()), vertexesMap.get(target.getElementId()), minimapScale);

        EdgeProperty originEdgeProperty = edgeElementWrapper.getElement().getEdgeProperty();
        overviewEdge.getEdgeProperty().bind(originEdgeProperty);
        overviewEdge.getEdgeProperty().strokeWidth.bind(
                Bindings.createDoubleBinding(() -> {
                    double minValue = graphOverviewProperty.minEdgeStrokeWidth.get();
                    double maxValue = graphOverviewProperty.maxEdgeStrokeWidth.get();
                    double value = Math.sqrt(originEdgeProperty.strokeWidth.get()) / 3;
                    if (value < minValue) {
                        value = minValue;
                    } else if (value > maxValue) {
                        value = maxValue;
                    }
                    return value;
                })
        );
        edgesMap.put(overviewEdge.getGraphElementId(), overviewEdge);
        return overviewEdge;
    }

    private final ChangeListener<Bounds> contentResize = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
            Bounds scrollPaneLayoutBounds = newValue;
            double scrollPaneWidth = scrollPaneLayoutBounds.getWidth();
            double scrollPaneHeight = scrollPaneLayoutBounds.getHeight();
            double ratioWidth = canvas.getWidth() / scrollPaneWidth;
            double ratioHeight = canvas.getHeight() / scrollPaneHeight;

            changeMinimapScale(ratioWidth, ratioHeight);
        }
    };

    private void setScaleTransform(Node minimapNode) {
        minimapNode.getTransforms().add(minimapScale);
    }

    protected void changeMinimapScale(double ratioWidth, double ratioHeight) {
        minimapScale.setX(ratioWidth);
        minimapScale.setY(ratioHeight);
    }

    @Override
    public void bind(GraphViewData graphViewData, GraphViewProperty graphViewProperty) {
        this.graphViewData = graphViewData;
        this.graphViewProperty = graphViewProperty;
        this.graphOverviewProperty = graphViewProperty.graphOverviewProperty;

        GraphViewModel graphViewModel = this.graphViewData.getGraphViewModel();
        graphViewModel.verticesProperty().addListener(vertexesListener);
        graphViewModel.edgesProperty().addListener(edgesListener);

        this.getChildren().add(canvas);
        this.getChildren().add(rectangle);

        rectangle.borderProperty().bind(this.graphOverviewProperty.rectangleBorder);
        canvas.setBackground(new Background(
                new BackgroundFill(
                        Color.WHITE,
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                )
        ));

        this.graphViewData.getScrollPane().getContent().layoutBoundsProperty().addListener(contentResize);

        for (VertexElementWrapper value : graphViewModel.getVertices().values()) {
            addVertexToMinimap(value);
        }
        for (EdgeElementWrapper value : graphViewModel.getEdges().values()) {
            addEdgeToMinimap(value);
        }
    }

    @Override
    public void unbind() {
        this.graphViewData.getScrollPane().getContent().layoutBoundsProperty().removeListener(contentResize);
        GraphViewModel graphViewModel = this.graphViewData.getGraphViewModel();
        graphViewModel.verticesProperty().removeListener(vertexesListener);
        graphViewModel.edgesProperty().removeListener(edgesListener);
        this.graphViewData = null;
        this.graphOverviewProperty = null;
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            updateRectangleArea(graphViewData.getScrollPane(), canvas, rectangle);
        });
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public DoubleProperty fitWidthProperty() {
        return canvas.prefWidthProperty();
    }

    @Override
    public DoubleProperty fitHeightProperty() {
        return canvas.prefHeightProperty();
    }

    public GraphOverviewData getData() {
        return data;
    }

    @Override
    public Image snapshot() {
        return SnapshotUtils.snapshot(canvas);
    }

    private Node createVertex(VertexElement element) {
        Node copyWithBindings = element.copyNodeForMinimap();
        if (copyWithBindings != null) {
            setScaleTransform(copyWithBindings);
            return copyWithBindings;
        }

        return createImage(element);
    }

    private ImageView createImage(Node node) {
        Bounds scrollPaneLayoutBounds = graphViewData.getScrollPane().getContent().getLayoutBounds();
        double scrollPaneWidth = scrollPaneLayoutBounds.getWidth();
        double scrollPaneHeight = scrollPaneLayoutBounds.getHeight();

        Bounds layoutBounds = node.getLayoutBounds();
        int width = (int) layoutBounds.getWidth();
        int height = (int) layoutBounds.getHeight();

        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(node.getLayoutX(), node.getLayoutY(), width, height));
        WritableImage snapshot = node.snapshot(snapshotParameters, null);

        return scaleImage(
                new ImageView(snapshot),
                (int) (width * canvas.getWidth() / scrollPaneWidth),
                (int) (height * canvas.getHeight() / scrollPaneHeight),
                true
        );
    }

    private ImageView scaleImage(ImageView imageView, int targetWidth, int targetHeight, boolean preserveRatio) {
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(Math.max(targetWidth, graphOverviewProperty.minVertexWidth.get()));
        imageView.setFitHeight(Math.max(targetHeight, graphOverviewProperty.minVertexHeight.get()));
        return imageView;
    }

    private void updateRectangleArea(ScrollPane scrollPane, Pane pane, Region rectangle) {
        Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
        double layoutWidth = layoutBounds.getWidth();
        double layoutHeight = layoutBounds.getHeight();

        Bounds viewportBounds = scrollPane.getViewportBounds();
        double x = (-viewportBounds.getMinX()) / layoutWidth;
        double y = (-viewportBounds.getMinY()) / layoutHeight;

        // используется, чтобы квадрат не выходил за границы карты, когда элемент выходит и "растягивает" canvas
        double width = viewportBounds.getWidth() / layoutWidth;
        if (x + width > 1) {
            width = 1 - x;
        }
        double height = viewportBounds.getHeight() / layoutHeight;
        if (y + height > 1) {
            height = 1 - y;
        }

        double fitWidth = pane.getWidth();
        double fitHeight = pane.getHeight();
        rectangle.setLayoutX(fitWidth * x);
        rectangle.setLayoutY(fitHeight * y);

        rectangle.setMinWidth(fitWidth * width);
        rectangle.setMaxWidth(fitWidth * width);

        rectangle.setMinHeight(fitHeight * height);
        rectangle.setMaxHeight(fitHeight * height);
    }

}
