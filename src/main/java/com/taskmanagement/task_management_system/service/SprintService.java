package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Sprint;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.repository.SprintRepository;
import com.taskmanagement.task_management_system.service.planning.SprintPlanningStrategy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SprintService {

	private final ProjectService projectService;
	private final SprintPlanningStrategy sprintPlanningStrategy;
	private final SprintRepository sprintRepository;

	public SprintService(ProjectService projectService, SprintPlanningStrategy sprintPlanningStrategy, SprintRepository sprintRepository) {
		this.projectService = projectService;
		this.sprintPlanningStrategy = sprintPlanningStrategy;
		this.sprintRepository = sprintRepository;
	}

	public Sprint createSprint(int projectId, Date startDate, Date endDate) {
		return projectService.createSprint(projectId, startDate, endDate);
	}

	@Transactional
	public List<Task> planTasksForSprint(int projectId, int sprintId, int capacity) {
		Project project = projectService.getProject(projectId);

		Sprint sprint = project.getSprints().stream()
				.filter(s -> s.getSprintId() == sprintId)
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Sprint " + sprintId + " not found in project " + projectId));

		List<Task> candidateTasks = new ArrayList<>(project.getTasks());
		List<Task> selectedTasks = sprintPlanningStrategy.selectTasks(candidateTasks, capacity);

		for (Task task : selectedTasks) {
			sprint.planTask(task);
		}

		return selectedTasks;
	}

	@Transactional
	public Sprint startSprint(int sprintId) {
		Sprint sprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new ResourceNotFoundException("Sprint " + sprintId + " not found"));
		sprint.startSprint();
		return sprintRepository.save(sprint);
	}
}
