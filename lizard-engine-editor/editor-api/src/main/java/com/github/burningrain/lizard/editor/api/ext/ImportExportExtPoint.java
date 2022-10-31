package com.github.burningrain.lizard.editor.api.ext;

import org.pf4j.ExtensionPoint;
import com.github.burningrain.lizard.engine.api.data.ProcessData;

public interface ImportExportExtPoint extends ExtensionPoint {

    ProcessData read(String name, byte[] bytes);

    byte[] write(ProcessData processData);

    String getExtension();

}
