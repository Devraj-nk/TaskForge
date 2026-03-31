package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Sprint;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.model.Team;
import com.taskmanagement.task_management_system.repository.ProjectRepository;
import com.taskmanagement.task_management_system.repository.TeamRepository;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final TeamRepository teamRepository;

	public ProjectService(
			ProjectRepository projectRepository,
			TeamRepository teamRepository
	) {
		this.projectRepository = projectRepository;
		this.teamRepository = teamRepository;
	}

	public List<Project> listProjects() {
		return projectRepository.findAll();
	}

	public Project getProject(int projectId) {
		return projectRepository.findWithDetailsByProjectId(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project " + projectId + " not found"));
	}

	@Transactional
	public Project createProject(String name, String description) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Project name is required");
		}
		Project project = new Project();
		project.setName(name.trim());
		project.setDescription(description);
		return projectRepository.save(project);
	}

	@Transactional
	public Project updateProject(int projectId, String name, String description) {
		Project project = getProject(projectId);
		if (name != null && !name.isBlank()) {
			project.setName(name.trim());
		}
		if (description != null) {
			project.setDescription(description);
		}
		return projectRepository.save(project);
	}

	@Transactional
	public void deleteProject(int projectId) {
		Project project = getProject(projectId);
		projectRepository.delete(project);
	}

	@Transactional
	public Sprint createSprint(int projectId, Date startDate, Date endDate) {
		Project project = getProject(projectId);
		Sprint sprint = new Sprint();
		sprint.setStartDate(startDate);
		sprint.setEndDate(endDate);
		project.addSprint(sprint);
		projectRepository.save(project);
		return sprint;
	}

	@Transactional
	public void assignTeam(int projectId, int teamId) {
		Project project = getProject(projectId);
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new ResourceNotFoundException("Team " + teamId + " not found"));

		// Owning side is Team.project (FK project_id lives on team)
		team.setProject(project);
		project.setTeam(team);
		teamRepository.save(team);
	}

	@Transactional
	public Team createAndAssignTeam(int projectId, String teamName) {
		Project project = getProject(projectId);
		if (teamName == null || teamName.isBlank()) {
			throw new IllegalArgumentException("Team name is required");
		}

		Team team = new Team();
		team.setTeamName(teamName.trim());
		team.setProject(project);
		project.setTeam(team);
		return teamRepository.save(team);
	}
}
