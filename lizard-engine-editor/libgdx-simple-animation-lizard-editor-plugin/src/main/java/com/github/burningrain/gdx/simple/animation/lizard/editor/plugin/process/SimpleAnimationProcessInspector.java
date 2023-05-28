package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process;

import com.github.br.gdx.simple.animation.io.interpret.TypeEnum;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.ui.ComboBoxTableCell;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.preview.PreviewSingleton;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


public class SimpleAnimationProcessInspector implements NodeContainer {

    private final Button chooseAtlasButton = new Button("choose atlas");
    private final Button showPreviewButton = new Button("show preview");
    private final Label variablesLabel = new Label("Variables:");

    private TableView<VariableModel> variablesTableView;
    private TableColumn<VariableModel, String> nameTableColumn;
    private TableColumn<VariableModel, String> typeTableColumn;

    private final VBox vBox = new VBox();

    @Override
    public void init(LizardUiApi lizardUiApi) {
        // создание таблицы
        variablesTableView = new TableView<>();
        nameTableColumn = new TableColumn<>("name");
        typeTableColumn = new TableColumn<>("type");

        variablesTableView.getColumns().addAll(nameTableColumn, typeTableColumn);

        // настройка таблицы
        nameTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        Collection<String> types = Arrays.stream(TypeEnum.values()).map(TypeEnum::getValue).collect(Collectors.toList());
        typeTableColumn.setCellFactory(param -> new ComboBoxTableCell<>(types, param));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        variablesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        variablesTableView.setEditable(true);

        // добавление/удаление строк из таблицы предикатов
        variablesTableView.setRowFactory(param -> {
            TableRow<VariableModel> tableRow = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete row");
            deleteItem.setOnAction(event -> {
                variablesTableView.getItems().remove(tableRow.getItem());
            });
            contextMenu.getItems().addAll(deleteItem);
            tableRow.contextMenuProperty().bind(Bindings.when(tableRow.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

            return tableRow;
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem addRowMenuItem = new MenuItem("Add row");
        addRowMenuItem.setOnAction(event -> {
            VariableModel model = new VariableModel();
            model.setName("variable");
            model.setType(TypeEnum.INTEGER.getValue());
            variablesTableView.getItems().add(model);
        });
        contextMenu.getItems().addAll(addRowMenuItem);
        variablesTableView.contextMenuProperty().set(contextMenu);
        // добавление/удаление строк из таблицы предикатов

        variablesTableView.setMinWidth(TableView.USE_COMPUTED_SIZE);
        variablesTableView.setPrefWidth(TableView.USE_COMPUTED_SIZE);
        variablesTableView.setMaxWidth(Double.MAX_VALUE);

        variablesTableView.setMinHeight(TableView.USE_COMPUTED_SIZE);
        variablesTableView.setPrefHeight(TableView.USE_COMPUTED_SIZE);
        variablesTableView.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(variablesTableView, Priority.ALWAYS);

        chooseAtlasButton.setOnAction(event -> {
            lizardUiApi.showOpenDialogChooserFile(
                    "Open texture atlas",
                    "Texture Atlas",
                    Collections.singletonList("*.atlas"),
                    file -> PreviewSingleton.getInstance().loadAtlasTexture(file.getAbsolutePath())
            );
        });
        showPreviewButton.setOnAction(event -> {
            PreviewSingleton.getInstance().showPreview(lizardUiApi.getStageX(), lizardUiApi.getStageY());
        });
        lizardUiApi.addOnCloseRequest(PreviewSingleton.getInstance().getConsumerOnWindowEvent());

        vBox.setPadding(new Insets(4, 4, 4, 4));
        vBox.setSpacing(4);

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(chooseAtlasButton, showPreviewButton);
        ButtonBar.setButtonData(chooseAtlasButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(showPreviewButton, ButtonBar.ButtonData.RIGHT);

        vBox.getChildren().addAll(buttonBar, variablesLabel, variablesTableView);
    }

    @Override
    public Node getNode() {
        return vBox;
    }

    public void bindModel(SimpleAnimationProcessModel model) {
        variablesTableView.itemsProperty().bindBidirectional(model.variablesProperty());
    }

    public void unbindModel(SimpleAnimationProcessModel model) {
        variablesTableView.itemsProperty().unbindBidirectional(model.variablesProperty());
    }

}