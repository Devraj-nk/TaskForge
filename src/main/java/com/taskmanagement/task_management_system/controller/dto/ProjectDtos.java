package com.taskmanagement.task_management_system.controller.dto;

import java.util.Date;
import java.util.List;

public final class ProjectDtos {
    private ProjectDtos() {
    }

    public record CreateProjectRequest(String name, String description) {
    }

    public record UpdateProjectRequest(String name, String description) {
    }

    public record CreateSprintRequest(Date startDate, Date endDate) {
    }

    public record ProjectSummary(int projectId, String name, String description) {
    }

    public record SprintSummary(int sprintId, Date startDate, Date endDate) {
    }

    public record TaskSummary(int taskId, String title, String description, int priority, String status, Integer assignedToUserId) {
    }

    public record ProjectDetails(
            int projectId,
            String name,
            String description,
            Integer teamId,
            String teamName,
            List<SprintSummary> sprints,
            List<TaskSummary> tasks
    ) {
    }
}
