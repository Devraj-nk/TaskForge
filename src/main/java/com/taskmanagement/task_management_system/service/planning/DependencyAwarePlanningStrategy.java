package com.taskmanagement.task_management_system.service.planning;

import com.taskmanagement.task_management_system.model.Dependency;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.model.TaskStatus;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DependencyAwarePlanningStrategy implements SprintPlanningStrategy {

    @Override
    public List<Task> selectTasks(List<Task> candidateTasks, int capacity) {
        if (capacity <= 0) {
            return List.of();
        }

        return candidateTasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.COMPLETED)
                .sorted(
                        Comparator.comparing(this::hasBlockedDependencies)
                                .thenComparingInt(Task::getPriority)
                )
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
}
