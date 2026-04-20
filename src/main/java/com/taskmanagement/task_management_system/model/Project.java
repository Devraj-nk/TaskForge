package com.taskmanagement.task_management_system.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "project")
/**
 * **Design patterns used:**
 * - Domain Model (JPA Entity): this class is a persistence-backed domain entity.
 * - Builder: `Project.builder()...build()` provides controlled object creation.
 *
 * **GRASP:**
 * - Information Expert: owns and maintains its `tasks` and `sprints` collections and the
 *   bidirectional links to `Task`/`Sprint`.
 * - Creator: `addTask()` creates a new `Task` when needed.
 *
 * **SOLID:**
 * - SRP: represents Project state + invariants (relationship consistency).
 */
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private int projectId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	// Relationships from the UML
	@OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Team team; // 1:1 (Team owns FK project_id)

	@Transient
	private ProjectManager manager; // not part of SQL schema

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sprint> sprints = new ArrayList<>(); // 1:N

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>(); // 1:N

	public Project() {
	}

	public Project(int projectId, String name, String description) {
		this.projectId = projectId;
		this.name = name;
		this.description = description;
	}

	/**
	 * Builder pattern entry-point.
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private Integer projectId;
		private String name;
		private String description;

		private Builder() {
		}

		public Builder projectId(int projectId) {
			this.projectId = projectId;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Project build() {
			Project project = new Project();
			if (projectId != null) {
				project.setProjectId(projectId);
			}
			project.setName(name);
			project.setDescription(description);
			return project;
		}
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
		// GRASP (Low Coupling) + SOLID (SRP)
		return Collections.unmodifiableList(sprints);
	}

	public void addSprint(Sprint sprint) {
		// GRASP (Information Expert): Project is responsible for keeping its Sprint relationship consistent.
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
		// GRASP (Low Coupling) 
		return Collections.unmodifiableList(tasks);
	}

	public void addTask(Task task) {
		// GRASP (Information Expert): Project owns tasks and maintains the back-reference.
		Objects.requireNonNull(task, "task");
		if (!tasks.contains(task)) {
			tasks.add(task);
			task.setProject(this);
		}
	}

	/**
	 * UML: +void addTask()
	 *
	 * Adds a new Task instance to this project.
	 */
	public void addTask() {
		// GRASP (Creator): Project creates a Task because it aggregates/contains Tasks.
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
