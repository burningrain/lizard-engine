package com.github.burningrain.lizard.editor.ui.model.defaultmodel;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import com.github.burningrain.lizard.editor.ui.utils.FxUtils;

import java.util.List;
import java.util.function.Consumer;

public class DefaultInspectorPaneController implements NodeContainer {

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<KeyValueFxBean> paramsTableView;

    @FXML
    private TableColumn<KeyValueFxBean, String> keyColumn;

    @FXML
    private TableColumn<KeyValueFxBean, String> valueColumn;

    @FXML
    private TableColumn<KeyValueFxBean, Boolean> showColumn;


    private ListChangeListener<KeyValueFxBean> tableListener;
    private MapChangeListener<String, String> modelListener;

    private KeyValueFxBean.Listener listener;

    private DefaultGraphElementModel model;


    public void init(LizardUiApi lizardUiApi) {
        // редактирование столбцов
        paramsTableView.setEditable(true);
        Callback<TableColumn<KeyValueFxBean, String>, TableCell<KeyValueFxBean, String>> cellFactory = p -> new EditingCell();
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        keyColumn.setCellFactory(cellFactory);
        keyColumn.setOnEditCommit(event -> paramsTableView.getItems().get(event.getTablePosition().getRow()).setKey(event.getNewValue()));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setCellFactory(cellFactory);
        valueColumn.setOnEditCommit(event -> paramsTableView.getItems().get(event.getTablePosition().getRow()).setValue(event.getNewValue()));

        showColumn.setCellValueFactory(kv -> kv.getValue().showProperty());
        showColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        // контекстное меню
        paramsTableView.setRowFactory(new Callback<TableView<KeyValueFxBean>, TableRow<KeyValueFxBean>>() {
            @Override
            public TableRow<KeyValueFxBean> call(TableView<KeyValueFxBean> tableView) {
                TableRow<KeyValueFxBean> tableRow = new TableRow<>();

                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteItem = new MenuItem("Delete row");
                deleteItem.setOnAction(event -> {
                    tableView.getItems().remove(tableRow.getItem());
                });
                contextMenu.getItems().addAll(deleteItem);
                tableRow.contextMenuProperty().bind(Bindings.when(tableRow.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

                return tableRow;
            }
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem addRowMenuItem = new MenuItem("Add row");
        addRowMenuItem.setOnAction(event -> {
            lizardUiApi.createStageChild(stage -> {
                Stage addPropertyWindow = createAddPropertyWindow(stage, "New Property");
                Platform.runLater(() -> {
                    AddPropertyController addPropertyController = new AddPropertyController();
                    Parent parent = FxUtils.loadFxml(addPropertyController, "/fxml/default_prop_inspector_add_property.fxml");
                    addPropertyController.handleClick(new AddPropertyController.ClickHandler() {
                        @Override
                        public void onClickOK(AddPropertyController controller) {
                            executeWithoutModelListener(() -> {
                                paramsTableView.getItems().add(new KeyValueFxBean(
                                        controller.getKeyTextField().getText(),
                                        controller.getValueTextField().getText(),
                                        controller.getShowCheckBox().isSelected(),
                                        listener
                                ));
                            });

                            addPropertyWindow.close();
                        }

                        @Override
                        public void onClickCancel(AddPropertyController controller) {
                            addPropertyWindow.close();
                        }
                    });
                    addPropertyWindow.setScene(new Scene(parent));
                    addPropertyWindow.showAndWait();
                });
            });
        });
        contextMenu.getItems().addAll(addRowMenuItem);
        paramsTableView.contextMenuProperty().set(contextMenu);
    }

    public void bind(DefaultGraphElementModel model) {
        this.model = model;
        //todo че-то криво и страшно вышло. не хочется особо делать объект Event...
        listener = (type, oldKeyValue, newKeyValue, value, turnOn) -> {
            switch (type) {
                case SHOW_ON:
                    if (turnOn) {
                        model.getShowPropertiesSet().add(oldKeyValue);
                    } else {
                        model.getShowPropertiesSet().remove(oldKeyValue);
                    }
                    break;
                case KEY:
                case VALUE:
                    // кривота с отцепкой и добавлением слушателя, ну а что делать? юзер меняет ui, ui изменяет модель, модель изменяет ui...
                    executeWithoutModelListener(() -> {
                        model.getProperties().remove(oldKeyValue);
                        model.getProperties().put(newKeyValue, value);
                    });
                    if (turnOn) {
                        model.getShowPropertiesSet().remove(oldKeyValue);
                        model.getShowPropertiesSet().add(newKeyValue);
                    }
                    break;
            }
        };

        // инициализация таблички из модели
        model.getProperties().forEach((key, value) -> {
            this.getParamsTableView().getItems().add(new KeyValueFxBean(key, value, model.getShowPropertiesSet().contains(key), listener));
        });

        // модельный слушатель для таблицы
        tableListener = change -> {
            if (!change.next()) {
                return;
            }

            List<? extends KeyValueFxBean> addedSubList = change.getAddedSubList();
            if (!addedSubList.isEmpty()) {
                addedSubList.forEach((Consumer<KeyValueFxBean>) keyValueFxBean -> {
                    model.getProperties().put(keyValueFxBean.getKey(), keyValueFxBean.getValue());
                    if (keyValueFxBean.showProperty().get()) {
                        model.getShowPropertiesSet().add(keyValueFxBean.getKey());
                    }
                });
            }

            List<? extends KeyValueFxBean> removed = change.getRemoved();
            if (!removed.isEmpty()) {
                removed.forEach((Consumer<KeyValueFxBean>) keyValueFxBean -> {
                    model.getProperties().remove(keyValueFxBean.getKey());
                    model.getShowPropertiesSet().remove(keyValueFxBean.getKey());
                });
            }
        };
        this.getParamsTableView().getItems().addListener(tableListener);

        // табличный слушатель для модели
        modelListener = change -> {
            if (change.wasAdded()) {
                this.getParamsTableView().getItems().add(new KeyValueFxBean(change.getKey(), change.getValueAdded(), false, listener));
            }

            if (change.wasRemoved()) {
                this.getParamsTableView().getItems().remove(new KeyValueFxBean(change.getKey(), null, false, null));
            }
        };
        model.getProperties().addListener(modelListener);
    }

    public void unbind(DefaultGraphElementModel model) {
        this.getParamsTableView().getItems().forEach(KeyValueFxBean::removeListener);
        this.getParamsTableView().getItems().removeListener(tableListener);
        model.getProperties().removeListener(modelListener);
    }

    public TableView<KeyValueFxBean> getParamsTableView() {
        return paramsTableView;
    }

    @Override
    public Node getNode() {
        return root;
    }

    private static Stage createAddPropertyWindow(Stage stage, String title) {
        Stage modalWindow = new Stage();
        modalWindow.setResizable(false);
        modalWindow.setTitle(title);

        modalWindow.initModality(Modality.WINDOW_MODAL);
        modalWindow.initOwner(stage);

        return modalWindow;
    }

    //todo ну и изврат это все
    private void executeWithoutModelListener(Runnable runnable) {
        model.getProperties().removeListener(modelListener);
        runnable.run();
        model.getProperties().addListener(modelListener);
    }


    private static class EditingCell extends TableCell<KeyValueFxBean, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }


}
