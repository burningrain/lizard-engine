package com.github.burningrain.gvizfx.overview;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.SnapshotUtils;
import com.github.burningrain.gvizfx.property.GraphOverviewProperty;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

//todo придется скрывать за интерфейсом методы...
public class GraphOverviewSnapshotImpl extends Pane implements GraphOverview {

    private final ImageView imageView = new ImageView();
    private final Region rectangle = new Region();

    private final GraphOverviewData data = new GraphOverviewData(
            this, rectangle, imageView::getFitWidth, imageView::getFitHeight);
    private GraphViewData graphViewData;
    private GraphOverviewProperty graphViewProperty;

    public DoubleProperty fitWidthProperty() {
        return imageView.fitWidthProperty();
    }

    public DoubleProperty fitHeightProperty() {
        return imageView.fitHeightProperty();
    }

    public void bind(GraphViewData graphViewData, GraphViewProperty graphViewProperty) {
        this.graphViewProperty = graphViewProperty.graphOverviewProperty;
        this.graphViewData = graphViewData;

        this.getChildren().add(imageView);
        this.getChildren().add(rectangle);

        rectangle.borderProperty().bind(this.graphViewProperty.rectangleBorder);
    }

    @Override
    public void unbind() {
        rectangle.borderProperty().unbind();
        this.getChildren().remove(imageView);
        this.getChildren().remove(rectangle);
        this.graphViewProperty = null;
        this.graphViewData = null;
    }

    @Override
    public void update() {
        Platform.runLater(() ->{
            imageView.setImage(SnapshotUtils.snapshot(graphViewData.getScrollPane().getContent()));
            updateRectangleArea(graphViewData.getScrollPane(), imageView, rectangle);
        });
    }

    @Override
    public Node getNode() {
        return this;
    }

    private void updateRectangleArea(ScrollPane scrollPane, ImageView imageView, Region rectangle) {
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

        double fitWidth = imageView.getFitWidth();
        double fitHeight = imageView.getFitHeight();
        rectangle.setLayoutX(fitWidth * x);
        rectangle.setLayoutY(fitHeight * y);

        rectangle.setMinWidth(fitWidth * width);
        rectangle.setMaxWidth(fitWidth * width);

        rectangle.setMinHeight(fitHeight * height);
        rectangle.setMaxHeight(fitHeight * height);
    }

    public GraphOverviewData getData() {
        return data;
    }

}
