package com.github.burningrain.gvizfx.overview;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.handlers.GraphElementHandlers;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.DragContext;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.gvizfx.model.GraphViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.util.function.Consumer;

public class GraphOverviewHandlersBinder implements GraphHandlersBinder {

    private final DragContext dragContext = new DragContext();
    private final GraphOverviewData graphOverviewData;

    private EventHandler<MouseEvent> onMousePressedEventHandler;
    private EventHandler<MouseEvent> onMouseDraggedEventHandler;
    private EventHandler<MouseEvent> onMouseReleasedEventHandler;

    // этот хак исключительно из-за того, что Bounds не меняются при кручении полосы прокрутки
    private ChangeListener<Number> vValueListener;

    // этот хак исключительно из-за того, что Bounds не меняются при кручении полосы прокрутки
    private ChangeListener<Number> hValueListener;


    private InvalidationListener rectangleMoveListener;

    private boolean wasDragged = false;
    private Consumer<MouseEvent> vertexEdgeMouseDraggedListener;
    private Consumer<MouseEvent> vertexEdgeMouseReleasedListener;

    private InvalidationListener updateListener;

    public GraphOverviewHandlersBinder(GraphOverviewData graphOverviewData) {
        this.graphOverviewData = graphOverviewData;
    }

    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        onMousePressedEventHandler = event -> {

            Node node = (Node) event.getSource();
            node.toFront();

//            double scale = graphView.getScale();
//
//            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
//            dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

            dragContext.x = node.getBoundsInParent().getMinX() - event.getScreenX();
            dragContext.y = node.getBoundsInParent().getMinY() - event.getScreenY();

            node.getScene().setCursor(Cursor.MOVE);
        };

        onMouseReleasedEventHandler = mouseEvent -> {
            Node node = (Node) mouseEvent.getSource();
            node.getScene().setCursor(Cursor.DEFAULT);
        };

        onMouseDraggedEventHandler = event -> {
            Node node = (Node) event.getSource();

            double offsetX = event.getScreenX() + dragContext.x;
            double offsetY = event.getScreenY() + dragContext.y;

            if(offsetX < 0) {
                offsetX = 0;
            }

            if(offsetY < 0) {
                offsetY = 0;
            }

            double maxX = graphOverviewData.getWidth() - node.getBoundsInParent().getWidth();
            if(offsetX > maxX) {
                offsetX = maxX;
            }

            double maxY = graphOverviewData.getHeight() - node.getBoundsInParent().getHeight();
            if(offsetY > maxY) {
                offsetY = maxY;
            }

            // adjust the offset in case we are zoomed
//            double scale = graphView.getScale();
//
//            offsetX /= scale;
//            offsetY /= scale;

            node.relocate(offsetX, offsetY);
        };

        vValueListener = (observable, oldValue, newValue) -> {
            ScrollPane scrollPane = viewData.getScrollPane();
            Region rectangle = graphOverviewData.getRectangle();
            double fitHeight = graphOverviewData.getHeight();

            Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
            double layoutHeight = layoutBounds.getHeight();

            Bounds viewportBounds = scrollPane.getViewportBounds();
            double scrollHeight = viewportBounds.getHeight();

            double y = newValue.doubleValue() * (layoutHeight - scrollHeight) / layoutHeight;
            if (y < 0) {
                y = 0;
            }
            double height = rectangle.getHeight() / fitHeight;
            if (y + height > 1) {
                y = 1 - height;
            }
            rectangle.layoutYProperty().removeListener(rectangleMoveListener);
            rectangle.setLayoutY(fitHeight * y);
            rectangle.layoutYProperty().addListener(rectangleMoveListener);
        };

        hValueListener = (observable, oldValue, newValue) -> {
            ScrollPane scrollPane = viewData.getScrollPane();
            Region rectangle = graphOverviewData.getRectangle();
            double fitWidth = graphOverviewData.getWidth();

            Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
            double layoutWidth = layoutBounds.getWidth();

            Bounds viewportBounds = scrollPane.getViewportBounds();
            double scrollWidth = viewportBounds.getWidth();

            double x = newValue.doubleValue() * (layoutWidth - scrollWidth) / layoutWidth;
            if (x < 0) {
                x = 0;
            }
            double width = rectangle.getWidth() / fitWidth;
            if (x + width > 1) {
                x = 1 - width;
            }

            rectangle.layoutXProperty().removeListener(rectangleMoveListener);
            rectangle.setLayoutX(fitWidth * x);
            rectangle.layoutXProperty().addListener(rectangleMoveListener);
        };

        rectangleMoveListener = observable -> {
            ScrollPane scrollPane = viewData.getScrollPane();
            Region rectangle = graphOverviewData.getRectangle();
            double fitWidth = graphOverviewData.getWidth();
            double fitHeight = graphOverviewData.getHeight();

            //fixme хак, отрубаем лиснетеры, делаем дело, врубаем
            scrollPane.vvalueProperty().removeListener(vValueListener);
            scrollPane.hvalueProperty().removeListener(hValueListener);

            Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
            double layoutWidth = layoutBounds.getWidth();
            double layoutHeight = layoutBounds.getHeight();

            Bounds viewportBounds = scrollPane.getViewportBounds();
            double scrollWidth = viewportBounds.getWidth();
            double scrollHeight = viewportBounds.getHeight();

            scrollPane.hvalueProperty().set((rectangle.getLayoutX()) / (fitWidth * (layoutWidth - scrollWidth) / layoutWidth));
            scrollPane.vvalueProperty().set((rectangle.getLayoutY()) / (fitHeight * (layoutHeight - scrollHeight) / layoutHeight));

            scrollPane.vvalueProperty().addListener(vValueListener);
            scrollPane.hvalueProperty().addListener(hValueListener);
        };

        vertexEdgeMouseDraggedListener = mouseEvent -> wasDragged = true;
        vertexEdgeMouseReleasedListener = mouseEvent -> {
            if (wasDragged) {
                graphOverviewData.getGraphOverview().update();
                wasDragged = false;
            }
        };

        updateListener = observable -> graphOverviewData.getGraphOverview().update();

        GraphViewModel graphViewModel = viewData.getGraphViewModel();
        Region rectangle = graphOverviewData.getRectangle();
        ScrollPane scrollPane = viewData.getScrollPane();

        rectangle.setOnMousePressed(onMousePressedEventHandler);
        rectangle.setOnMouseDragged(onMouseDraggedEventHandler);
        rectangle.setOnMouseReleased(onMouseReleasedEventHandler);

        rectangle.layoutXProperty().addListener(rectangleMoveListener);
        rectangle.layoutYProperty().addListener(rectangleMoveListener);

        scrollPane.vvalueProperty().addListener(vValueListener);
        scrollPane.hvalueProperty().addListener(hValueListener);

        GraphElementHandlers vertexHandlers = handlers.getVertexHandlers();
        GraphElementHandlers edgeHandlers = handlers.getEdgeHandlers();

        //fixme это надо оптимизировать. жуть как тормозит
        //fixme как вариант, кешировать внешний вид нод и рисовать на миникарте ручками все.
        graphViewModel.getVertices().addListener(updateListener);
        graphViewModel.getEdges().addListener(updateListener);

        vertexHandlers.getEventFilter(MouseEvent.MOUSE_DRAGGED).addListener(vertexEdgeMouseDraggedListener);
        vertexHandlers.getEventFilter(MouseEvent.MOUSE_RELEASED).addListener(vertexEdgeMouseReleasedListener);
        edgeHandlers.getEventFilter(MouseEvent.MOUSE_RELEASED).addListener(vertexEdgeMouseReleasedListener);

        //todo здесь подумать. Может все засунуть это в слушатель отрисовки какой-нибудь
        //Platform.runLater(() -> {
        scrollPane.widthProperty().addListener(updateListener);
        scrollPane.heightProperty().addListener(updateListener);
        //});
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager,
                       GraphViewProperty graphViewProperty) {
        Region rectangle = graphOverviewData.getRectangle();

        rectangle.setOnMousePressed(null);
        rectangle.setOnMouseDragged(null);
        rectangle.setOnMouseReleased(null);
        onMousePressedEventHandler = null;
        onMouseDraggedEventHandler = null;
        onMouseReleasedEventHandler = null;

        GraphElementHandlers vertexHandlers = handlers.getVertexHandlers();
        GraphElementHandlers edgeHandlers = handlers.getEdgeHandlers();

        vertexHandlers.getEventFilter(MouseEvent.MOUSE_DRAGGED).addListener(vertexEdgeMouseDraggedListener);
        vertexHandlers.getEventFilter(MouseEvent.MOUSE_RELEASED).addListener(vertexEdgeMouseReleasedListener);
        edgeHandlers.getEventFilter(MouseEvent.MOUSE_RELEASED).addListener(vertexEdgeMouseReleasedListener);
        updateListener = null;
        wasDragged = false;

        //todo поотцеплять все
    }

}
