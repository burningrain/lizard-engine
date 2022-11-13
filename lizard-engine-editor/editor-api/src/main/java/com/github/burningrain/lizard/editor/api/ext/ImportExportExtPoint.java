package com.github.burningrain.lizard.editor.api.ext;

import com.github.burningrain.lizard.editor.api.LizardPluginApi;
import org.pf4j.ExtensionPoint;
import com.github.burningrain.lizard.engine.api.data.ProcessData;

public interface ImportExportExtPoint extends ExtensionPoint {

    ProcessData read(LizardPluginApi pluginApi, String name, byte[] bytes);

    byte[] write(LizardPluginApi pluginApi, ProcessData processData);

    String getExtension();

}
