package com.github.burningrain.lizard.editor.ui.components.controllers;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.gvizfx.grid.BackgroundGrid;
import com.github.burningrain.gvizfx.grid.DotGrid;
import com.github.burningrain.gvizfx.grid.LineGrid;
import com.github.burningrain.gvizfx.property.GridProperty;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GridSettingsController {

    @FXML
    private ComboBox<GridType> gridTypeComboBox;

    @FXML
    private CheckBox showGridCheckBox;

    @FXML
    private CheckBox snapToGridCheckBox;

    @FXML
    private ColorPicker gridColorPicker;

    @FXML
    private TextField strokeOffsetTextField;

    @FXML
    private TextField gridSpacingTextField;

    @FXML
    private Button applyBtn;

    private GraphView graphView;

    private InvalidationListener invalidationListener;
    private EventHandler<ActionEvent> applyEventHandler;


    public void bind(GraphView graphView) {
        this.graphView = graphView;
        GridProperty model = this.graphView.getProperty().gridProperty;

        gridTypeComboBox.getItems().addAll(GridType.values());
        invalidationListener = observable -> {
            showGridCheckBox.selectedProperty().set(model.isShowGrid.get());
            snapToGridCheckBox.selectedProperty().set(model.isSnapToGrid.get());
            strokeOffsetTextField.textProperty().set(model.strokeOffset.get() + "");
            gridSpacingTextField.textProperty().set(model.gridSpacing.get() + "");
            gridColorPicker.valueProperty().set(model.color.get());

            SingleSelectionModel<GridType> selectionModel = gridTypeComboBox.getSelectionModel();
            selectionModel.select(gridToType(this.graphView.getGrid().getClass()));
        };

        applyEventHandler = event -> setModelFromUI();
        applyBtn.setOnAction(applyEventHandler);
        bindListenersToModel(model);
    }

    public void unbind() {
        unbindListenersFromModel();

        applyBtn.setOnAction(null);
        this.applyEventHandler = null;
        this.graphView = null;
    }

    private void bindListenersToModel(GridProperty model) {
        model.strokeOffset.addListener(invalidationListener);
        model.gridSpacing.addListener(invalidationListener);
        model.isSnapToGrid.addListener(invalidationListener);
        model.isShowGrid.addListener(invalidationListener);
        model.color.addListener(invalidationListener);

        invalidationListener.invalidated(null);
    }

    private void unbindListenersFromModel() {
        GridProperty model = this.graphView.getProperty().gridProperty;
        model.strokeOffset.removeListener(invalidationListener);
        model.gridSpacing.removeListener(invalidationListener);
        model.isSnapToGrid.removeListener(invalidationListener);
        model.isShowGrid.removeListener(invalidationListener);
        model.color.removeListener(invalidationListener);
    }

    private void setModelFromUI() {
        unbindListenersFromModel();
        GridProperty model = this.graphView.getProperty().gridProperty;

        model.strokeOffset.set(Double.parseDouble(strokeOffsetTextField.textProperty().get()));
        model.gridSpacing.set(Double.parseDouble(gridSpacingTextField.textProperty().get()));
        model.isSnapToGrid.set(snapToGridCheckBox.selectedProperty().get());
        model.isShowGrid.set(showGridCheckBox.selectedProperty().get());
        model.color.set(gridColorPicker.valueProperty().get());

        if(showGridCheckBox.selectedProperty().get()) {
            SingleSelectionModel<GridType> selectionModel = gridTypeComboBox.getSelectionModel();
            graphView.setGrid(typeToGrid(selectionModel.selectedItemProperty().get()));
        }
        bindListenersToModel(model);
    }

    private static BackgroundGrid typeToGrid(GridType newValue) {
        return switch (newValue) {
            case LINE_GRID -> new LineGrid();
            case DOT_GRID -> new DotGrid();
        };
    }

    private static GridType gridToType(Class<? extends BackgroundGrid> gridClass) {
        if(gridClass.isAssignableFrom(LineGrid.class)) {
            return GridType.LINE_GRID;
        } else if(gridClass.isAssignableFrom(DotGrid.class)) {
            return GridType.DOT_GRID;
        } else {
            throw new RuntimeException("gridClass=[" + gridClass.getName() + "] is unknown");
        }
    }

    private enum GridType {
        LINE_GRID, DOT_GRID
    }

}
