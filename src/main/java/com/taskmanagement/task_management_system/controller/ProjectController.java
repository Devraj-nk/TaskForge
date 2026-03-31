package com.taskmanagement.task_management_system.controller;

import com.taskmanagement.task_management_system.controller.dto.ProjectDtos;
import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.service.ProjectService;
import com.taskmanagement.task_management_system.service.TaskService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	private final ProjectService projectService;
	private final TaskService taskService;

	public ProjectController(ProjectService projectService, TaskService taskService) {
		this.projectService = projectService;
		this.taskService = taskService;
	}

	@GetMapping
	public List<ProjectDtos.ProjectSummary> listProjects() {
		return projectService.listProjects().stream()
				.map(p -> new ProjectDtos.ProjectSummary(p.getProjectId(), p.getName(), p.getDescription()))
				.toList();
	}

	@GetMapping("/{projectId}")
	public ProjectDtos.ProjectDetails getProject(@PathVariable int projectId) {
		Project project = projectService.getProject(projectId);
		List<ProjectDtos.SprintSummary> sprints = project.getSprints().stream()
				.map(s -> new ProjectDtos.SprintSummary(s.getSprintId(), s.getStartDate(), s.getEndDate()))
				.toList();
		List<ProjectDtos.TaskSummary> tasks = project.getTasks().stream()
				.map(this::toTaskSummary)
				.toList();
		Integer teamId = project.getTeam() != null ? project.getTeam().getTeamId() : null;
		String teamName = project.getTeam() != null ? project.getTeam().getTeamName() : null;
		return new ProjectDtos.ProjectDetails(
				project.getProjectId(),
				project.getName(),
				project.getDescription(),
				teamId,
				teamName,
				sprints,
				tasks
		);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProjectDtos.ProjectSummary createProject(@RequestBody ProjectDtos.CreateProjectRequest request) {
		Project project = projectService.createProject(request.name(), request.description());
		return new ProjectDtos.ProjectSummary(project.getProjectId(), project.getName(), project.getDescription());
	}

	@PutMapping("/{projectId}")
	public ProjectDtos.ProjectSummary updateProject(
			@PathVariable int projectId,
			@RequestBody ProjectDtos.UpdateProjectRequest request
	) {
		Project project = projectService.updateProject(projectId, request.name(), request.description());
		return new ProjectDtos.ProjectSummary(project.getProjectId(), project.getName(), project.getDescription());
	}

	@DeleteMapping("/{projectId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProject(@PathVariable int projectId) {
		projectService.deleteProject(projectId);
	}

	@PostMapping("/{projectId}/tasks")
	@ResponseStatus(HttpStatus.CREATED)
	public ProjectDtos.TaskSummary createTask(
			@PathVariable int projectId,
			@RequestBody com.taskmanagement.task_management_system.controller.dto.TaskDtos.CreateTaskRequest request
	) {
		Task task = taskService.createTask(projectId, request.title(), request.description(), request.priority());
		return toTaskSummary(task);
	}

	@GetMapping("/{projectId}/tasks")
	public List<ProjectDtos.TaskSummary> listProjectTasks(@PathVariable int projectId) {
		Project project = projectService.getProject(projectId);
		return project.getTasks().stream().map(this::toTaskSummary).toList();
	}

	@PutMapping("/{projectId}/team/{teamId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void assignTeam(@PathVariable int projectId, @PathVariable int teamId) {
		projectService.assignTeam(projectId, teamId);
	}

	private ProjectDtos.TaskSummary toTaskSummary(Task task) {
		Integer assignedToId = task.getAssignedTo() != null ? task.getAssignedTo().getUserId() : null;
		return new ProjectDtos.TaskSummary(
				task.getTaskId(),
				task.getTitle(),
				task.getDescription(),
				task.getPriority(),
				task.getStatus() != null ? task.getStatus().name() : null,
				assignedToId
		);
	}
}
