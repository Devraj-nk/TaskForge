package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProjectManager extends User {
	// Relationships from the UML
	private final List<Project> managedProjects = new ArrayList<>(); // manages 0..* projects
	private Team team; // directed association to Team

	public ProjectManager() {
		super();
	}

	public ProjectManager(int userId, String name, String email, String password) {
		super(userId, name, email, password);
	}

	public List<Project> getManagedProjects() {
		return Collections.unmodifiableList(managedProjects);
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * UML: +void createProject()
	 * Creates a new Project and adds it to the projects this manager manages.
	 */
	public void createProject() {
		Project project = new Project();
		project.setProjectId(managedProjects.size() + 1);
		project.setManager(this);
		if (this.team != null) {
			project.setTeam(this.team);
		}
		managedProjects.add(project);
	}

	/**
	 * UML: +void createSprint()
	 * Creates a new Sprint inside the most recently created project (if any).
	 */
	public void createSprint() {
		if (managedProjects.isEmpty()) {
			return;
		}
		Project project = managedProjects.get(managedProjects.size() - 1);
		Sprint sprint = new Sprint(project.getSprints().size() + 1);
		sprint.setProject(project);
		project.addSprint(sprint);
	}

	/**
	 * UML: +void assignTask()
	 * Minimal placeholder that preserves the UML usage dependency between PM and Task.
	 */
	public void assignTask() {
		// Intentionally minimal: actual assignment would require Task/TeamMember state.
	}

	/**
	 * UML: +void defineDependencies()
	 * Minimal placeholder that preserves the UML dependency behavior.
	 */
	public void defineDependencies() {
		// Intentionally minimal: actual dependency wiring would require Dependency/Task state.
	}

	// Helpful overloads (optional) while still keeping UML methods above.
	public void manageProject(Project project) {
		Objects.requireNonNull(project, "project");
		if (!managedProjects.contains(project)) {
			managedProjects.add(project);
			project.setManager(this);
		}
	}
}
