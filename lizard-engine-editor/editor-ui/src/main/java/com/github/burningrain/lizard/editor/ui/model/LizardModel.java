package com.github.burningrain.lizard.editor.ui.model;

import java.io.Serializable;

public interface LizardModel<D extends Serializable> extends Serializable {

    D getData();

}
