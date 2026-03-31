package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Dependency;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.repository.DependencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DependencyService {

	private final TaskService taskService;
	private final DependencyRepository dependencyRepository;

	public DependencyService(TaskService taskService, DependencyRepository dependencyRepository) {
		this.taskService = taskService;
		this.dependencyRepository = dependencyRepository;
	}

	@Transactional
	public Dependency createDependency(int prerequisiteTaskId, int dependentTaskId, String dependencyType) {
		Task prerequisite = taskService.getTask(prerequisiteTaskId);
		Task dependent = taskService.getTask(dependentTaskId);
		if (dependencyType == null || dependencyType.isBlank()) {
			dependencyType = "FINISH_TO_START";
		}
		Dependency dependency = new Dependency();
		dependency.setDependencyType(dependencyType.trim());
		dependency.setPrerequisiteTask(prerequisite);
		dependency.setDependentTask(dependent);
		dependent.addDependency(dependency);
		return dependencyRepository.save(dependency);
	}
}
