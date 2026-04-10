package com.taskmanagement.task_management_system.service.planning;

import com.taskmanagement.task_management_system.model.Dependency;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.model.TaskStatus;
import java.util.Comparator;
import java.util.List;

public final class SprintPlanner {

    private final boolean dependencyAware;

    private SprintPlanner(Builder builder) {
        this.dependencyAware = builder.dependencyAware;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Task> selectTasks(List<Task> candidateTasks, int capacity) {
        if (capacity <= 0) {
            return List.of();
        }

        Comparator<Task> comparator = Comparator.comparingInt(Task::getPriority);
        if (dependencyAware) {
            comparator = Comparator.comparing(this::hasBlockedDependencies).thenComparingInt(Task::getPriority);
        }

        return candidateTasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.COMPLETED)
                .sorted(comparator)
                .limit(capacity)
                .toList();
    }

    private boolean hasBlockedDependencies(Task task) {
        for (Dependency dependency : task.getDependencies()) {
            if (!dependency.isSatisfied()) {
                return true;
            }
        }
        return false;
    }

    public static final class Builder {
        private boolean dependencyAware;

        private Builder() {
        }

        public Builder dependencyAware(boolean dependencyAware) {
            this.dependencyAware = dependencyAware;
            return this;
        }

        public SprintPlanner build() {
            return new SprintPlanner(this);
        }
    }
}
