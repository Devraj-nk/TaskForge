package com.taskmanagement.task_management_system.service.planning;

import com.taskmanagement.task_management_system.model.Task;
import java.util.List;

public interface SprintPlanningStrategy {

    List<Task> selectTasks(List<Task> candidateTasks, int capacity);
}
