package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Project {
	private int projectId;
	private String name;
	private String description;

	// Relationships from the UML
	private Team team; // composition with Team
	private ProjectManager manager; // managed by a ProjectManager
	private final List<Sprint> sprints = new ArrayList<>(); // composition with Sprint
	private final List<Task> tasks = new ArrayList<>(); // composition with Task

	public Project() {
	}

	public Project(int projectId, String name, String description) {
		this.projectId = projectId;
		this.name = name;
		this.description = description;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public ProjectManager getManager() {
		return manager;
	}

	public void setManager(ProjectManager manager) {
		this.manager = manager;
	}

	public List<Sprint> getSprints() {
		return Collections.unmodifiableList(sprints);
	}

	public void addSprint(Sprint sprint) {
		Objects.requireNonNull(sprint, "sprint");
		if (!sprints.contains(sprint)) {
			sprints.add(sprint);
			sprint.setProject(this);
		}
	}

	public void removeSprint(Sprint sprint) {
		if (sprints.remove(sprint)) {
			if (sprint != null && sprint.getProject() == this) {
				sprint.setProject(null);
			}
		}
	}

	public List<Task> getTasks() {
		return Collections.unmodifiableList(tasks);
	}

	public void addTask(Task task) {
		Objects.requireNonNull(task, "task");
		if (!tasks.contains(task)) {
			tasks.add(task);
		}
	}

	/**
	 * UML: +void addTask()
	 *
	 * Adds a new Task instance to this project.
	 */
	public void addTask() {
		addTask(new Task());
	}

	/**
	 * UML: +void closeProject()
	 *
	 * Minimal implementation: clears the composed aggregates (tasks/sprints).
	 */
	public void closeProject() {
		tasks.clear();
		for (Sprint sprint : new ArrayList<>(sprints)) {
			sprint.setProject(null);
		}
		sprints.clear();
	}
}
