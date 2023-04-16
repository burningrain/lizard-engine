package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge;

import com.github.br.gdx.simple.animation.io.interpret.OperatorEnum;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.Constants;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process.SimpleAnimationProcessModel;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process.VariableModel;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.ui.ComboBoxTableCell;
import com.github.burningrain.lizard.editor.api.LizardUiApi;
import com.github.burningrain.lizard.editor.api.NodeContainer;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EdgePropertiesInspector implements NodeContainer {

    private final Label transitionTitle = new Label();

    private TableView<PredicateModel> predicatesTableView;
    private TableColumn<PredicateModel, String> variableNameTableColumn;
    private TableColumn<PredicateModel, String> operatorTableColumn;
    private TableColumn<PredicateModel, String> valueTableColumn;

    private final VBox vBox = new VBox();

    @Override
    public void init(LizardUiApi lizardUiApi) {
        // создание таблицы
        predicatesTableView = new TableView<>();
        variableNameTableColumn = new TableColumn<>("variable");
        operatorTableColumn = new TableColumn<>("operator");
        valueTableColumn = new TableColumn<>("value");

        predicatesTableView.getColumns().addAll(variableNameTableColumn, operatorTableColumn, valueTableColumn);

        // настройка таблицы
        Collection<VariableModel> variables = getVariables(lizardUiApi);
        List<String> varNameList = variables.stream().map(VariableModel::getName).collect(Collectors.toList());

        variableNameTableColumn.setCellFactory(param -> new ComboBoxTableCell<>(varNameList, param));
        variableNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("variableName"));

        Collection<String> operators = Arrays.stream(OperatorEnum.values()).map(OperatorEnum::getValue).collect(Collectors.toList());
        operatorTableColumn.setCellFactory(param -> new ComboBoxTableCell<>(operators, param));
        operatorTableColumn.setCellValueFactory(new PropertyValueFactory<>("operator"));

        valueTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        predicatesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        predicatesTableView.setEditable(true);

        // добавление/удаление строк из таблицы предикатов
        predicatesTableView.setRowFactory(param -> {
            TableRow<PredicateModel> tableRow = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete row");
            deleteItem.setOnAction(event -> {
                predicatesTableView.getItems().remove(tableRow.getItem());
            });
            contextMenu.getItems().addAll(deleteItem);
            tableRow.contextMenuProperty().bind(Bindings.when(tableRow.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

            return tableRow;
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem addRowMenuItem = new MenuItem("Add row");
        addRowMenuItem.setOnAction(event -> {
            PredicateModel model = new PredicateModel();

            String defaultVarName;
            Iterator<String> iterator = varNameList.iterator();
            if(iterator.hasNext()) {
                defaultVarName = iterator.next();
            } else {
                defaultVarName = ""; //fixme модальное окно с сообщением, что переменных у процесса нет нет
            }
            model.setVariableName(defaultVarName);
            model.setOperator(OperatorEnum.EQ.getValue());
            model.setValue("");
            predicatesTableView.getItems().add(model);
        });
        contextMenu.getItems().addAll(addRowMenuItem);
        predicatesTableView.contextMenuProperty().set(contextMenu);
        // добавление/удаление строк из таблицы предикатов

        vBox.getChildren().addAll(transitionTitle, predicatesTableView);
    }

    private Collection<VariableModel> getVariables(LizardUiApi lizardUiApi) {
        SimpleAnimationProcessModel model = lizardUiApi.getDataModelForCurrentProject(Constants.PLUGIN_ID);
        return model.getVariables();
    }

    @Override
    public Node getNode() {
        return vBox;
    }

    public void bindModel(SimpleAnimationEdgeModel model) {
        transitionTitle.setText(model.getFrom() + "->" + model.getTo());

        predicatesTableView.itemsProperty().bindBidirectional(model.fsmPredicatesProperty());
    }

    public void unbindModel(SimpleAnimationEdgeModel model) {
        predicatesTableView.itemsProperty().unbindBidirectional(model.fsmPredicatesProperty());
    }

}
