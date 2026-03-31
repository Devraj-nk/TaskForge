package com.taskmanagement.task_management_system.controller;

import com.taskmanagement.task_management_system.controller.dto.ProjectDtos;
import com.taskmanagement.task_management_system.controller.dto.TaskDtos;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.model.TaskStatus;
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
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public List<ProjectDtos.TaskSummary> listTasks() {
		return taskService.listTasks().stream().map(this::toSummary).toList();
	}

	@GetMapping("/{taskId}")
	public ProjectDtos.TaskSummary getTask(@PathVariable int taskId) {
		return toSummary(taskService.getTask(taskId));
	}

	@PutMapping("/{taskId}")
	public ProjectDtos.TaskSummary updateTask(@PathVariable int taskId, @RequestBody TaskDtos.UpdateTaskRequest request) {
		TaskStatus status = null;
		if (request.status() != null && !request.status().isBlank()) {
			status = TaskStatus.valueOf(request.status().trim().toUpperCase());
		}
		Task updated = taskService.updateTask(taskId, request.title(), request.description(), request.priority(), status);
		return toSummary(updated);
	}

	@DeleteMapping("/{taskId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTask(@PathVariable int taskId) {
		taskService.deleteTask(taskId);
	}

	@PostMapping("/{taskId}/assign")
	public ProjectDtos.TaskSummary assignTask(@PathVariable int taskId, @RequestBody TaskDtos.AssignTaskRequest request) {
		return toSummary(taskService.assignTask(taskId, request.teamMemberId()));
	}

	private ProjectDtos.TaskSummary toSummary(Task task) {
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
