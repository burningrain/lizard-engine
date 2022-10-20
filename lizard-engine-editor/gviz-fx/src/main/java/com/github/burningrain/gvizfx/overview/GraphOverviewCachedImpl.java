package com.github.burningrain.gvizfx.overview;

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
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.util.Map;

public class GraphOverviewCachedImpl extends Pane implements GraphOverview {

    private final Pane canvas = new Pane();
    private final Region rectangle = new Region();

    private final GraphOverviewData data = new GraphOverviewData(this, rectangle, canvas::getWidth, canvas::getHeight);

    private GraphViewData graphViewData;
    private GraphViewProperty graphViewProperty;
    private GraphOverviewProperty graphOverviewProperty;

    private final ObservableMap<String, Node> vertexesMap = FXCollections.observableHashMap();
    private final ObservableMap<String, Node> edgesMap = FXCollections.observableHashMap();

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
        overviewEdge.bind(vertexesMap.get(source.getElementId()), vertexesMap.get(target.getElementId()));

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
            GraphViewModel viewModel = graphViewData.getGraphViewModel();
            for (Map.Entry<String, Node> entry : vertexesMap.entrySet()) {
                resizeGraphNode(viewModel.verticesProperty().get(entry.getKey()).getElement(), entry.getValue(), newValue);
            }
            for (Map.Entry<String, Node> entry : edgesMap.entrySet()) {
                //TODO
            }
        }
    };

    private void resizeGraphNode(Node originElement, Node minimapNode, Bounds scrollPaneLayoutBounds) {
        double scrollPaneWidth = scrollPaneLayoutBounds.getWidth();
        double scrollPaneHeight = scrollPaneLayoutBounds.getHeight();

        Bounds nodeLayoutBounds = originElement.getLayoutBounds();
        int width = (int) nodeLayoutBounds.getWidth();
        int height = (int) nodeLayoutBounds.getHeight();

        int newWidth = (int) (width * canvas.getWidth() / scrollPaneWidth);
        int newHeight = (int) (height * canvas.getHeight() / scrollPaneHeight);

        if (minimapNode instanceof ImageView imageView) {
            scaleImage(imageView, newWidth, newHeight, true);
        } else {
            setNodeWidthAndHeight(minimapNode, newWidth, newHeight, (float)height/width);
        }
    }

    protected void setNodeWidthAndHeight(Node minimapNode, int newWidth, int newHeight, float ratio) {
        double w = Math.max(newWidth, graphOverviewProperty.minVertexWidth.get());
        double h = Math.max(newHeight, graphOverviewProperty.minVertexHeight.get());

        double rw = h / ratio;
        double rh = w * ratio;
        if(minimapNode instanceof Region region) {
            region.setMaxHeight(w);
            region.setMinWidth(w);
            region.setPrefWidth(w);

            region.setPrefHeight(h);
            region.setMinHeight(h);
            region.setMaxHeight(h);
        } else if(minimapNode instanceof Shape shape) {
            if(shape instanceof Circle circle) {
                double d = (newWidth + newHeight) >> 1;
                double r = d / 2;
                circle.setRadius(r);
                circle.setCenterX(r);
                circle.setCenterY(r);
            } else if (shape instanceof Rectangle rectangle) {
                rectangle.setWidth(w);
                rectangle.setHeight(h);
            } else if (shape instanceof Text text) {
                text.setWrappingWidth(w);
            } else if (shape instanceof Path path) {
                //TODO подумать над веми этими ифами
            } else if (shape instanceof Line line) {

            } else if (shape instanceof CubicCurve cubicCurve) {

            } else if (shape instanceof Ellipse ellipse) {

            } else if (shape instanceof SVGPath) {

            } else if (shape instanceof Arc) {

            } else if (shape instanceof Polygon) {

            } else if (shape instanceof Polyline polyline) {

            } else if (shape instanceof QuadCurve quadCurve) {

            }
        }

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

    private Node createVertex(VertexElement element) {
        Node copyWithBindings = element.copyNodeForMinimap();
        if (copyWithBindings != null) {
            resizeGraphNode(element, copyWithBindings, this.graphViewData.getScrollPane().getContent().getLayoutBounds());
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
