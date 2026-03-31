package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Sprint;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class SprintService {

	private final ProjectService projectService;

	public SprintService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public Sprint createSprint(int projectId, Date startDate, Date endDate) {
		return projectService.createSprint(projectId, startDate, endDate);
	}
}
