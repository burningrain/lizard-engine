package com.github.burningrain.lizard.editor.ui.model;

import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class IOWrapper {

    private int count;
    private HashMap<String, List<ImportExportExtPoint>> extensionsMap = new HashMap<>();

    public void add(String pluginId, ImportExportExtPoint point) {
        List<ImportExportExtPoint> importExportExtPoints = extensionsMap.computeIfAbsent(pluginId, k -> new ArrayList<>());
        importExportExtPoints.add(point);
        count++;
    }

    public List<ImportExportExtPoint> getPoints(String pluginId) {
        return extensionsMap.get(pluginId);
    }

    public ImportExportExtPoint getFirst() {
        return extensionsMap.entrySet().iterator().next().getValue().get(0);
    }

    public Collection<String> getPlugins() {
        return extensionsMap.keySet();
    }

    public int getSize() {
        return count;
    }

}
