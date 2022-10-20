package com.github.burningrain.lizard.editor.ui.draggers;

import javafx.scene.input.DataFormat;

public class InnerDataFormat extends DataFormat {

    public static final InnerDataFormat PROCESS_ELEMENT = new InnerDataFormat("PROCESS_ELEMENT");

    InnerDataFormat(String s) {
        super(s);
    }

}
