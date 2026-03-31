package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.model.TaskStatus;
import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.repository.ProjectRepository;
import com.taskmanagement.task_management_system.repository.TaskRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;
	private final TeamService teamService;

	public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, TeamService teamService) {
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.teamService = teamService;
	}

	public List<Task> listTasks() {
		return taskRepository.findAll();
	}

	public List<Task> listTasksAssignedTo(int userId) {
		return taskRepository.findByAssignedTo_UserId(userId);
	}

	public Task getTask(int taskId) {
		return taskRepository.findById(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("Task " + taskId + " not found"));
	}

	@Transactional
	public Task createTask(int projectId, String title, String description, int priority) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("Task title is required");
		}
		if (priority < 1 || priority > 3) {
			throw new IllegalArgumentException("Task priority must be 1 (HIGH), 2 (MEDIUM), or 3 (LOW)");
		}

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project " + projectId + " not found"));

		Task task = new Task();
		task.setTitle(title.trim());
		task.setDescription(description);
		task.setPriority(priority);
		task.setProject(project);
		project.addTask(task);
		return taskRepository.save(task);
	}

	@Transactional
	public Task updateTask(int taskId, String title, String description, Integer priority, TaskStatus status) {
		Task task = getTask(taskId);
		if (title != null && !title.isBlank()) {
			task.setTitle(title.trim());
		}
		if (description != null) {
			task.setDescription(description);
		}
		if (priority != null) {
			if (priority < 1 || priority > 3) {
				throw new IllegalArgumentException("Task priority must be 1 (HIGH), 2 (MEDIUM), or 3 (LOW)");
			}
			task.setPriority(priority);
		}
		if (status != null) {
			task.updateStatus(status);
		}
		return taskRepository.save(task);
	}

	@Transactional
	public void deleteTask(int taskId) {
		// Note: removing it from any project's task list is out-of-scope without a reverse index.
		getTask(taskId);
		taskRepository.deleteById(taskId);
	}

	@Transactional
	public Task assignTask(int taskId, int teamMemberId) {
		Task task = getTask(taskId);
		TeamMember member = teamService.getMember(teamMemberId);
		task.setAssignedTo(member);
		member.addAssignedTask(task);
		return taskRepository.save(task);
	}
}
