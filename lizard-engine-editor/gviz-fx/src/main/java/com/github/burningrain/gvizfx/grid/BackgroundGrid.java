package com.github.burningrain.gvizfx.grid;

import com.github.burningrain.gvizfx.property.GridProperty;
import javafx.scene.Node;

//FIXME отображение сетки сейчас очень затратно и по памяти и по процессору. Надо что-то делать
public interface BackgroundGrid {

    void init(GridProperty gridProperty);

    void relocateAndResize(double x0, double y0, double pWidth, double pHeight, double spacing);

    double getWidth();

    double getHeight();

    Node getNode();

}
