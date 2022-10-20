package com.github.burningrain.gvizfx;

import com.sun.javafx.geom.Point2D;

public final class Intersector {

    //fixme кривой метод для общего назначения
    //todo страх. отрефакторить на массивах
    public static Point2D intersectSegmentAndRectangle(double x1, double y1, double x2, double y2,
                                                       double leftX, double rightX, double topY, double bottomY) {
        // верх прямоугольника
        Point2D point2D = Intersector.intersectSections(
                x1, y1, x2, y2,
                leftX, topY, rightX, topY
        );
        if(point2D == null) {
            // низ прямоугольника
            point2D = Intersector.intersectSections(
                    x1, y1, x2, y2,
                    leftX, bottomY, rightX, bottomY
            );
            if(point2D == null) {
                // левая сторона прямоугольника
                point2D = Intersector.intersectSections(
                        x1, y1, x2, y2,
                        leftX, topY, leftX, bottomY
                );
                if(point2D == null) {
                    // правая сторона прямоугольника
                    point2D = Intersector.intersectSections(
                            x1, y1, x2, y2,
                            rightX, topY, rightX, bottomY
                    );
                }
            }
        }
        return point2D;
    }


    public static Point2D intersectSections(double x11, double y11, double x12, double y12,
                                            double x21, double y21, double x22, double y22
    ) {
        return intersectSections((float) x11, (float) y11, (float) x12, (float) y12, (float) x21, (float) y21, (float) x22, (float) y22);
    }

    /**
     * находит точку пересечения двух отрезков
     *
     * @param x11
     * @param y11
     * @param x12
     * @param y12
     * @param x21
     * @param y21
     * @param x22
     * @param y22
     * @return
     */
    public static Point2D intersectSections(float x11, float y11, float x12, float y12,
                                            float x21, float y21, float x22, float y22
    ) {
        float a1 = y12 - y11;
        float b1 = x12 - x11;
        float c1 = x11 * y12 - x12 * y11;

        float a2 = y22 - y21;
        float b2 = x22 - x21;
        float c2 = x21 * y22 - x22 * y21;

        float znam = a1 * b2 - a2 * b1;

        // не различаем случая, когда прямые параллельны или совпадают
        if (znam == 0) {
            return null;
        }

        Point2D point = new Point2D();
        point.setLocation((c1 * b2 - c2 * b1) / znam, -(a1 * c2 - a2 * c1) / znam);

        // выход за пределы отрезков
        float eps = 0.1f; // погрешность из-за плавающей точки
        if ((point.x + eps < Math.min(x11, x12)  || point.x - eps > Math.max(x11, x12)) ||
                (point.y + eps < Math.min(y11, y12)  || point.y - eps > Math.max(y11, y12)) ||
                (point.x + eps < Math.min(x21, x22)  || point.x - eps > Math.max(x21, x22)) ||
                (point.y + eps < Math.min(y21, y22)  || point.y - eps > Math.max(y21, y22))
            ) {
            return null;
        }

        return point;
    }


}
