package com.github.burningrain.lizard.editor.api;

import java.io.Serializable;
import java.util.Map;

public interface ElementDataConverter<M extends Serializable> {

    Map<String, String> exportNodeData(M model);

    M importNodeData(Map<String, String> data);

}
