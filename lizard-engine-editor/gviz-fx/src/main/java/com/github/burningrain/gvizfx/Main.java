package com.github.burningrain.gvizfx;

import com.github.burningrain.gvizfx.element.VertexElement;
import com.github.burningrain.gvizfx.element.edge.curve.CurveEdgeFactory;
import com.github.burningrain.gvizfx.property.GraphViewProperty;
import com.github.burningrain.gvizfx.element.EdgeElement;
import com.github.burningrain.gvizfx.grid.LineGrid;
import com.github.burningrain.gvizfx.model.GraphViewModel;
import com.github.burningrain.gvizfx.overview.GraphOverview;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();

        root.getChildren().add(createLine());

        GraphView graph = createGraph();
        root.getChildren().add(graph);

        GraphOverview overview = graph.getOverview();
        overview.fitWidthProperty().set(220);
        overview.fitHeightProperty().set(220);
        root.getChildren().add(overview.getNode());

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node createLine() {
        Label label = new Label("12345");
        label.setPrefWidth(0);
        label.setPrefHeight(0);

        return label;

    }

    private static GraphView createGraph() {
        //GraphView graphView = new GraphView(new GraphViewProperty(), null);
        GraphView graphView = new GraphView().setGrid(new LineGrid());
//        new Thread(() -> {
//            try {
////                Thread.sleep(10_000);
////                Platform.runLater(() -> {
////                    graphView.getProperty().gridProperty.snapToGrid.set(true);
////                });
//                Thread.sleep(10_000);
//                Platform.runLater(() -> {
//                    graphView.getProperty().gridProperty.isShowGrid.set(false);
//                });
//                Thread.sleep(10_000);
//                Platform.runLater(() -> {
//                    graphView.getProperty().gridProperty.isShowGrid.set(true);
//                });
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
        GraphViewProperty property = graphView.getProperty();
        property.gridProperty.isSnapToGrid.set(true);
        //property.elementModelProperty.edgeProperty.strokeWidth.set(10);
        //property.elementModelProperty.arrowProperty.length.set(50);
        property.elementModelProperty.edgeProperty.color.set(Color.YELLOWGREEN);

        GraphViewModel graphViewModel = graphView.getGraphViewModel();
        //graphViewModel.setEdgeFactory(new CurveEdgeFactory());

        graphViewModel.addVertex(new VertexElementImpl("Vertex A"), 0, 0);
        graphViewModel.addVertex(new VertexElementImpl("Vertex B"), 20, 20);
        graphViewModel.addVertex(new VertexElementImpl("Vertex C"), 40, 40);
        graphViewModel.addVertex(new VertexElementImpl("Vertex D"), 90, 90);
        graphViewModel.addVertex(new VertexElementImpl("Vertex E"), 150, 150);
        graphViewModel.addVertex(new VertexElementImpl("Vertex F"), 200, 200);
        graphViewModel.addVertex(new VertexElementImpl("Vertex G"), 300, 300);

        graphViewModel.addEdge("a", true, "Vertex A", "Vertex B", new Label("a (Vertex A, Vertex B)"));
        graphViewModel.addEdge("b", false, "Vertex A", "Vertex C", new Label("b (Vertex A, Vertex C)"));
        graphViewModel.addEdge("c", true, "Vertex B", "Vertex C", new Label("c (Vertex B, Vertex C)"));
        graphViewModel.addEdge("d", false, "Vertex C", "Vertex D", new Label("d (Vertex C, Vertex D)"));
        EdgeElement edgeE = graphViewModel.addEdge("e", true, "Vertex B", "Vertex E", new Label("e (Vertex B, Vertex E)"));
        edgeE.setProperty(edgeE.getEdgeProperty().strokeWidth, 0.5);
        graphViewModel.addEdge("f", false, "Vertex D", "Vertex F", new Label("f (Vertex D, Vertex F)"));
        EdgeElement edgeG = graphViewModel.addEdge("g", true, "Vertex D", "Vertex G", new Label("g (Vertex D, Vertex G)"));
        //edgeG.setProperty(edgeG.getEdgeProperty().color, Color.RED);
        edgeG.setProperty(edgeG.getEdgeProperty().strokeWidth, 5);

        return graphView;
    }

    public static class VertexElementImpl extends VertexElement<Label> {

        public VertexElementImpl(String graphElementId) {
            super(graphElementId, new Label(graphElementId));
        }

        @Override
        public Label copyNodeForMinimap() {
            Label label = new Label(getGraphElementId());
            label.setPrefWidth(4);
            label.setPrefHeight(4);

            return label;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}