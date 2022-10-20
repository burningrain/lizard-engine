package com.github.burningrain.gvizfx.layout;


import javafx.scene.Node;

import java.util.Collection;

public interface Layout {

    void lay(Collection<Node> vertices);

}