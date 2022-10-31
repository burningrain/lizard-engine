package com.github.burningrain.planetbot.lizard.editor.plugin.vertex;

import com.github.burningrain.lizard.editor.api.*;
import com.github.burningrain.planetbot.lizard.editor.plugin.io.Constants;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

import static com.github.burningrain.planetbot.lizard.editor.plugin.io.Constants.*;

public class PlanetBotVertexFactory implements VertexFactory<PlanetBotVertexModel, Circle, PlanetBotVertexFactory.PlanetBotVertexInspector> {

    private final PropertiesInspectorBinder<PlanetBotVertexModel, PlanetBotVertexInspector> propertiesInspectorBinder =
            new PropertiesInspectorBinder<>() {

                @Override
                public void bindInspector(PlanetBotVertexModel model, PlanetBotVertexInspector inspectorNode) {
                    model.bindInspector(inspectorNode);
                }

                @Override
                public void unbindInspector(PlanetBotVertexModel model, PlanetBotVertexInspector inspectorNode) {
                    model.unbindInspector(inspectorNode);
                }

                @Override
                public PlanetBotVertexInspector createPropertiesInspector() {
                    PlanetBotVertexInspector inspector = new PlanetBotVertexInspector();

                    ComboBox<PlanetType> planetTypeComboBox = new ComboBox<>();
                    planetTypeComboBox.setId("planetTypeComboBox");
                    inspector.getChildren().add(planetTypeComboBox);

                    CheckBox isMainCheckBox = new CheckBox("is a base");
                    isMainCheckBox.setId("isMainCheckBox");
                    inspector.getChildren().add(isMainCheckBox);

                    VBox vBox = new VBox();
                    vBox.getChildren().add(planetTypeComboBox);
                    vBox.getChildren().add(isMainCheckBox);

                    vBox.setSpacing(8);
                    inspector.getChildren().add(vBox);
                    return inspector;
                }

            };

    private final VertexModelBinder<PlanetBotVertexModel, Circle> vertexModelBinder = new VertexModelBinder<>() {

        @Override
        public Circle createNode() {
            Circle circle = new Circle();
            circle.setFill(PlanetBotVertexModel.DEFAULT_COLOR);
            circle.setRadius(PlanetBotVertexModel.DEFAULT_RADIUS);
            circle.setCenterX(PlanetBotVertexModel.DEFAULT_RADIUS);
            circle.setCenterY(PlanetBotVertexModel.DEFAULT_RADIUS);

            return circle;
        }

        @Override
        public Class<Circle> getNodeClass() {
            return Circle.class;
        }

        @Override
        public void bindNodeToModel(Circle node, PlanetBotVertexModel model) {
            model.bindNode(node);
        }

        @Override
        public void unbindNodeToModel(Circle node, PlanetBotVertexModel model) {
            model.unbindNode();
        }

        @Override
        public PlanetBotVertexModel createNewNodeModel() {
            return new PlanetBotVertexModel();
        }
    };

    private final ElementDataConverter<PlanetBotVertexModel> elementDataConverter = new ElementDataConverter<>() {
        @Override
        public Map<String, String> exportNodeData(PlanetBotVertexModel model) {
            HashMap<String, String> map = new HashMap<>();

            map.put(PLANET_TYPE, String.valueOf(model.getPlanetType().getCode()));
            map.put(IS_START_POINT, String.valueOf(model.isBaseProperty().get()));
            map.put(OWNER_ID, String.valueOf(model.getOwnerId()));
            map.put(UNITS, String.valueOf(model.getUnits()));

            return map;
        }

        @Override
        public PlanetBotVertexModel importNodeData(Map<String, String> data) {
            PlanetBotVertexModel planetBotVertexModel = new PlanetBotVertexModel();

            planetBotVertexModel.setPlanetType(PlanetType.getByCode(Byte.parseByte(data.get(PLANET_TYPE))));
            planetBotVertexModel.setIsBase(Boolean.parseBoolean(data.get(IS_START_POINT)));
            planetBotVertexModel.setOwnerId(Integer.parseInt(data.get(OWNER_ID)));
            planetBotVertexModel.setUnits(Integer.parseInt(data.get(UNITS)));

            return planetBotVertexModel;
        }
    };

    @Override
    public String getTitle() {
        return Constants.VERTEX_TITLE;
    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public PropertiesInspectorBinder<PlanetBotVertexModel, PlanetBotVertexInspector> getElementInspectorBinder() {
        return propertiesInspectorBinder;
    }

    @Override
    public VertexModelBinder<PlanetBotVertexModel, Circle> getElementModelBinder() {
        return vertexModelBinder;
    }

    @Override
    public ElementDataConverter<PlanetBotVertexModel> getElementDataConverter() {
        return elementDataConverter;
    }

    public static class PlanetBotVertexInspector extends AnchorPane implements NodeContainer {
        @Override
        public void init(LizardUiApi lizardUiApi) {
        }

        @Override
        public Node getNode() {
            return this;
        }
    }

}
