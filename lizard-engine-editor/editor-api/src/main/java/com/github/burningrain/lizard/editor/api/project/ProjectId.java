package com.github.burningrain.lizard.editor.api.project;

import java.util.Objects;
import java.util.UUID;

public final class ProjectId implements Comparable<ProjectId> {

    private final UUID id;

    private ProjectId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public static ProjectId of(UUID id) {
        return new ProjectId(Objects.requireNonNull(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectId projectId = (ProjectId) o;
        return id.equals(projectId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(ProjectId o) {
        return id.compareTo(o.id);
    }
}
