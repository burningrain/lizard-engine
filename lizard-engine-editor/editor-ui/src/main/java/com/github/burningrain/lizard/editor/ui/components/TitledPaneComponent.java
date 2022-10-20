package com.github.burningrain.lizard.editor.ui.components;

import com.github.burningrain.lizard.editor.ui.draggers.VertexDragAndDrop;
import com.github.burningrain.lizard.editor.ui.model.ProcessElementType;
import com.github.burningrain.lizard.editor.ui.model.VertexFactoryWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;

import java.util.Collection;

public class TitledPaneComponent {

    private VertexDragAndDrop vertexDragAndDrop;

    private TitledPane titledPane;
    private StackPane stackPane;
    private ListView<VertexFactoryWrapper> listView;


    public TitledPaneComponent(String pluginId, VertexDragAndDrop vertexDragAndDrop, Collection<VertexFactoryWrapper> vertexFactoryWrappers) {
        this.vertexDragAndDrop = vertexDragAndDrop;

        stackPane = new StackPane();
        stackPane.getChildren().add(listView = createListView(vertexFactoryWrappers));

        titledPane = new TitledPane();
        titledPane.setText(pluginId);
        titledPane.setContent(stackPane);
    }

    public TitledPane getTitledPane() {
        return titledPane;
    }

    public ListView<VertexFactoryWrapper> getListView() {
        return listView;
    }

    private ListView<VertexFactoryWrapper> createListView(Collection<VertexFactoryWrapper> vertexFactoryWrappers) {
        ListView<VertexFactoryWrapper> listView = new ListView<>();
        setListViewDisplay(listView);
        listView.itemsProperty().set(FXCollections.observableArrayList(vertexFactoryWrappers));

        return listView;
    }

    private void setListViewDisplay(ListView<VertexFactoryWrapper> listView) {
        listView.setCellFactory(param -> {
            ListCell<VertexFactoryWrapper> listCell = new ListCell<VertexFactoryWrapper>() {
                @Override
                public void updateItem(VertexFactoryWrapper factory, boolean empty) {
                    super.updateItem(factory, empty);
                    if (!empty) {
                        ProcessElementType type = factory.getType();
                        setText(type.getElementName());
                        setGraphic(factory.getFactory().getImageView());
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            vertexDragAndDrop.applyToSource(listCell);
            return listCell;
        });
    }

}
