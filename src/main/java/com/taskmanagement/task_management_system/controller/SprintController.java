package com.taskmanagement.task_management_system.controller;

import com.taskmanagement.task_management_system.controller.dto.ProjectDtos;
import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Sprint;
import com.taskmanagement.task_management_system.service.ProjectService;
import com.taskmanagement.task_management_system.service.SprintService;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects/{projectId}/sprints")
public class SprintController {

	private final SprintService sprintService;
	private final ProjectService projectService;

	public SprintController(SprintService sprintService, ProjectService projectService) {
		this.sprintService = sprintService;
		this.projectService = projectService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProjectDtos.SprintSummary createSprint(
			@PathVariable int projectId,
			@RequestBody(required = false) ProjectDtos.CreateSprintRequest request
	) {
		Date start = request != null ? request.startDate() : null;
		Date end = request != null ? request.endDate() : null;
		Sprint sprint = sprintService.createSprint(projectId, start, end);
		return new ProjectDtos.SprintSummary(sprint.getSprintId(), sprint.getStartDate(), sprint.getEndDate());
	}

	@GetMapping
	public List<ProjectDtos.SprintSummary> listSprints(@PathVariable int projectId) {
		Project project = projectService.getProject(projectId);
		return project.getSprints().stream()
				.map(s -> new ProjectDtos.SprintSummary(s.getSprintId(), s.getStartDate(), s.getEndDate()))
				.toList();
	}
}
