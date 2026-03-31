package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Sprint;
import com.taskmanagement.task_management_system.model.Task;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectPlanningController {

    private final ProjectService projectService;
    private final SprintService sprintService;

    public ProjectPlanningController(ProjectService projectService, SprintService sprintService) {
        this.projectService = projectService;
        this.sprintService = sprintService;
    }

    @Transactional
    public Project createProjectWithTeam(String projectName, String description, int teamId) {
        Project project = projectService.createProject(projectName, description);
        projectService.assignTeam(project.getProjectId(), teamId);
        return project;
    }

    @Transactional
    public Sprint createAndPlanSprint(
            int projectId,
            Date startDate,
            Date endDate,
            int capacity
    ) {
        Sprint sprint = sprintService.createSprint(projectId, startDate, endDate);
        sprintService.planTasksForSprint(projectId, sprint.getSprintId(), capacity);
        return sprint;
    }

    @Transactional
    public List<Task> planExistingSprint(int projectId, int sprintId, int capacity) {
        return sprintService.planTasksForSprint(projectId, sprintId, capacity);
    }
}
