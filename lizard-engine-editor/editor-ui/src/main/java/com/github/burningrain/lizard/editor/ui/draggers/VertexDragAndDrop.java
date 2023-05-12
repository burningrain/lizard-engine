package com.github.burningrain.lizard.editor.ui.draggers;

import com.github.burningrain.gvizfx.GraphViewData;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.handlers.GraphViewHandlers;
import com.github.burningrain.gvizfx.handlers.binders.GraphHandlersBinder;
import com.github.burningrain.gvizfx.input.InputEventManager;
import com.github.burningrain.lizard.editor.ui.actions.impl.AddVertexToEditorAction;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.api.project.model.ProcessElementType;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.model.VertexFactoryWrapper;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.burningrain.lizard.editor.api.VertexFactory;
import com.github.burningrain.lizard.editor.api.VertexModelBinder;

@Component
public class VertexDragAndDrop implements GraphHandlersBinder {

    @Autowired
    private Store store;

    @Autowired
    private ActionManager actionManager;

    @Autowired
    private ActionFactory actionFactory;

    private EventHandler<MouseEvent> onDragDetectedHandler;
    private EventHandler<DragEvent> onDragOverHandler;
    private EventHandler<DragEvent> onDragExitHandler;
    private EventHandler<DragEvent> onDragDroppedHandler;

    public void applyToSource(ListCell<VertexFactoryWrapper> listCell) {
        onDragDetectedHandler = event -> {
            Dragboard dragboard = listCell.startDragAndDrop(TransferMode.ANY);
            ClipboardContent clipboardContent = new ClipboardContent();
            //todo кривота. 1 вкладка на 1 плагин
            VertexFactoryWrapper item = listCell.getItem();
            clipboardContent.put(InnerDataFormat.PROCESS_ELEMENT, item.getType());

            dragboard.setContent(clipboardContent);
            event.consume();
        };
        listCell.setOnDragDetected(onDragDetectedHandler);
    }

    // apply destination
    @Override
    public void apply(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        ScrollPane scrollPane = viewData.getScrollPane();

        onDragOverHandler = event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasContent(InnerDataFormat.PROCESS_ELEMENT)) {
                ProcessElementType elementType = (ProcessElementType) dragboard.getContent(InnerDataFormat.PROCESS_ELEMENT);
                VertexFactory vertexFactory = store.getProcessElements().get(elementType.getPluginId()).getVertexFactories().get(elementType.getElementName()).getFactory();
                VertexModelBinder modelBinder = (VertexModelBinder) vertexFactory.getElementModelBinder();
                Node newNode = modelBinder.createNode();
                newNode.setOpacity(0.3);
                WritableImage snapshot = newNode.snapshot(new SnapshotParameters(), null);
                ImageCursor imageCursor = new ImageCursor(snapshot);
                scrollPane.getScene().setCursor(imageCursor);

                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        };

        onDragExitHandler = event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasString()) {
                scrollPane.getScene().setCursor(Cursor.DEFAULT);
            }
            event.consume();
        };

        onDragDroppedHandler = event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasContent(InnerDataFormat.PROCESS_ELEMENT)) {
                ProcessElementType elementType = (ProcessElementType) dragboard.getContent(InnerDataFormat.PROCESS_ELEMENT);
                VertexFactoryWrapper vertexFactoryWrapper = store.getProcessElements().get(elementType.getPluginId()).getVertexFactories().get(elementType.getElementName());
                AddVertexToEditorAction action = actionFactory.createAction(AddVertexToEditorAction.class);
                Point2D point2D = viewData.getGraphView().translateToCanvasPoint(event.getX(), event.getY());
                action.setX((int) point2D.getX());
                action.setY((int) point2D.getY());
                action.setVertexFactoryWrapper(vertexFactoryWrapper);
                actionManager.executeAction(action);

                success = true;
            }
            scrollPane.getScene().setCursor(Cursor.DEFAULT);
            event.setDropCompleted(success);
            event.consume();
        };

        scrollPane.setOnDragOver(onDragOverHandler);
        scrollPane.setOnDragExited(onDragExitHandler);
        scrollPane.setOnDragDropped(onDragDroppedHandler);
    }

    @Override
    public void cancel(GraphViewData viewData, GraphViewHandlers handlers, InputEventManager inputEventManager, GraphViewProperty graphViewProperty) {
        ScrollPane scrollPane = viewData.getScrollPane();

        scrollPane.setOnDragOver(null);
        scrollPane.setOnDragExited(null);
        scrollPane.setOnDragDropped(null);
    }

}
