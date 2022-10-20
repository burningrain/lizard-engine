package com.github.burningrain.gvizfx.element.edge.line;

import com.github.burningrain.gvizfx.element.edge.UserNodeEdgeElement;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class UserNodeLineElement<U extends Node> extends UserNodeEdgeElement<U, Line> {

    private InvalidationListener recalculateElementListener;

    private final Rotate rotate = new Rotate();

    public UserNodeLineElement(U userNode) {
        super(userNode);
        userNode.getTransforms().add(rotate);
    }

    @Override
    public void bindElement(Line edge, Node source, Node target) {
        recalculateElementListener = observable -> {
            recalculateUserNode(edge);
        };
        edge.startXProperty().addListener(recalculateElementListener);
        edge.startYProperty().addListener(recalculateElementListener);
        edge.endXProperty().addListener(recalculateElementListener);
        edge.endYProperty().addListener(recalculateElementListener);

        recalculateElementListener.invalidated(null);
    }

    @Override
    public void unbindElement() {

    }

    @Override
    public void setStrokeWidth(Number newValue, Color color) {

    }

    @Override
    public void setColor(Paint newValue) {

    }

    private void recalculateUserNode(Line edge) {
        U userNode = getNode();

        double startX = edge.getStartX();
        double startY = edge.getStartY();
        double endX = edge.getEndX();
        double endY = edge.getEndY();

        double ax = endX - startX;
        double ay = endY - startY;

        double length2 = ax * ax + ay * ay;
        double width = userNode.getBoundsInLocal().getWidth();
        double width2 = width * width;

        if (length2 < width2) {
            // делать невидимой или убирать, или обрезать
            userNode.setOpacity(0);
            return;
        } else {
            // делать видимой, если была невидимой
            userNode.setOpacity(100); //todo а что дешевле, прозрачность или удаление/добавление ноды?
        }

        double length = Math.sqrt(length2);
        double diffLength = (length - width) / 2;

        // точка на прямой
        double dotX = ax * diffLength / length + startX;
        double dotY = ay * diffLength / length + startY;
        double degree = Math.toDegrees(Math.atan2(ay, ax));

        if(endX < startX) {
            dotX = -ax * diffLength / length + endX;
            dotY = -ay * diffLength / length + endY;
            degree -= Math.toDegrees(Math.PI);
        }

        //todo сейчас подписи снизу дуги. Если надо сверху, то считать нормаль правильно и поднимать на высоту
//        double height = userNode.getBoundsInLocal().getHeight();
        // нормаль прямой
//        double edgeNorma = 1.0 / Math.sqrt((-ax + ay) * (-ax + ay));
//        double deltaX = ay * edgeNorma;
//        double deltaY = -ax * edgeNorma;

        userNode.setLayoutX(dotX);
        userNode.setLayoutY(dotY);

        // расчет поворота
        rotate.setPivotX(0);
        rotate.setPivotY(0);
        rotate.setAngle(degree);
    }

}
