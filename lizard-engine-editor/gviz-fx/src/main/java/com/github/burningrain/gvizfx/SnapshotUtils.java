package com.github.burningrain.gvizfx;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

public final class SnapshotUtils {

    private SnapshotUtils(){}

    //fixme нарвался на баг fx, см. jdk-8088198, поэтому режу картинку на 512*512
    //fixme что думаю, надо снимать снепшот каждой ноды и рисовать их ручками со связями всеми
    public static WritableImage snapshot(Node content) {
        Bounds layoutBounds = content.getLayoutBounds();
        int width = (int) layoutBounds.getWidth();
        int height = (int) layoutBounds.getHeight();

        // todo если ширина и высота не определены, значит нода еще не отрисована
        if(width <= 0 || height <= 0) return null;

        int widthChunk = Math.min(width, 512);
        int heightChunk = Math.min(height, 512);

        int n = (int) Math.ceil(width / widthChunk);
        int m = (int) Math.ceil(height / heightChunk);
        final WritableImage result = new WritableImage(width, height);
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < m; row++) {
                int x = widthChunk * col;
                int y = heightChunk * row;
                int w = col != (n - 1)? widthChunk : width - x;
                int h = row != (m - 1)? heightChunk : height - y;
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setViewport(new Rectangle2D(x, y, w, h));
                WritableImage snapshot = content.snapshot(snapshotParameters, null);
                result.getPixelWriter().setPixels(x, y, w, h, snapshot.getPixelReader(), 0, 0);
            }
        }
        return result;
    }

}
