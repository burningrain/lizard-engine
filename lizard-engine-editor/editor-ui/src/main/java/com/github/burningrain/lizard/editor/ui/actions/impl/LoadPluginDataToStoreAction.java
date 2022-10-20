package com.github.burningrain.lizard.editor.ui.actions.impl;

import com.github.burningrain.lizard.editor.ui.actions.Actions;
import com.github.burningrain.lizard.editor.ui.model.IOWrapper;
import com.github.burningrain.lizard.editor.ui.model.ProcessElementsWrapper;
import com.github.burningrain.lizard.editor.ui.model.Store;
import javafx.collections.FXCollections;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;
import com.github.burningrain.lizard.editor.api.ext.ProcessElementsExtPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Component
public class LoadPluginDataToStoreAction implements NotRevertAction {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private Store store;

    @Override
    public String getId() {
        return Actions.LOAD_PLUGIN_DATA_TO_STORE;
    }

    @Override
    public void execute() {
        HashMap<String, List<ImportExportExtPoint>> extsMap = new HashMap<>();
        HashMap<String, ProcessElementsWrapper> processElementsMap = new HashMap<>();
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper startedPlugin : startedPlugins) {
            try {
                List<ProcessElementsExtPoint> elementsExtensions = pluginManager.getExtensions(ProcessElementsExtPoint.class, startedPlugin.getPluginId());
                processElementsMap.put(startedPlugin.getPluginId(), new ProcessElementsWrapper(startedPlugin.getPluginId(), elementsExtensions));
                List<ImportExportExtPoint> ioExtensions = pluginManager.getExtensions(ImportExportExtPoint.class, startedPlugin.getPluginId());
                extsMap.put(startedPlugin.getPluginId(), ioExtensions);
            } catch (Throwable t) {
                t.printStackTrace(); //todo в логгер
            }
        }
        store.setProcessElements(FXCollections.observableMap(processElementsMap));

        HashMap<String, IOWrapper> ioMap = new HashMap<>();
        for (Map.Entry<String, List<ImportExportExtPoint>> entry : extsMap.entrySet()) {
            String pluginId = entry.getKey();
            List<ImportExportExtPoint> points = entry.getValue();
            if(points != null && !points.isEmpty()) {
                for (ImportExportExtPoint point : points) {
                    IOWrapper ioWrapper = ioMap.get(point.getExtension());
                    if(ioWrapper == null) {
                        ioWrapper = new IOWrapper();
                        ioMap.put(point.getExtension(), ioWrapper);
                    }
                    ioWrapper.add(pluginId, point);
                }
            }
        }
        store.setIoPoints(FXCollections.observableMap(ioMap));
    }

}
