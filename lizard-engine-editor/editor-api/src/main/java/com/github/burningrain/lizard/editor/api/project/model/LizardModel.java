package com.github.burningrain.lizard.editor.api.project.model;

import java.io.Serializable;

public interface LizardModel<D extends Serializable> extends Serializable {

    D getData();

}
