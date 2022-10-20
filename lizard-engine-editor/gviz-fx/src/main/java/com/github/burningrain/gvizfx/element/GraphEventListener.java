package com.github.burningrain.gvizfx.element;

import com.github.burningrain.gvizfx.property.SimpleGraphElementProperty;

public interface GraphEventListener {

    void setElementModel(SimpleGraphElementProperty elementModel);

    SimpleGraphElementProperty getElementModel();

    void select();

    void deselect();

}
