package com.github.burningrain.lizard.editor.ui.utils;

import com.github.burningrain.lizard.editor.api.project.ProjectId;

import java.util.UUID;

public class ProjectUtils {

    public static ProjectId generateProjectId() {
        return ProjectId.of(UUID.randomUUID());
    }

    private static int counter = 0;

    public static String generateProjectTitle() {
        return "unnamed" + counter++;
    }

    public static ProjectId fromStringUUID(String id) {
        return ProjectId.of(UUID.fromString(id));
    }
}
