package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Sprint {

	private int sprintId;
	private Date startDate;
	private Date endDate;

	// Relationships from the UML
	private Project project; // composition with Project (Sprint belongs to exactly one Project)
	private final List<Task> plannedTasks = new ArrayList<>(); // aggregation with Task

	public Sprint() {
	}

	public Sprint(int sprintId) {
		this.sprintId = sprintId;
	}

	public Sprint(int sprintId, Date startDate, Date endDate) {
		this.sprintId = sprintId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getSprintId() {
		return sprintId;
	}

	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Task> getPlannedTasks() {
		return Collections.unmodifiableList(plannedTasks);
	}

	public void planTask(Task task) {
		Objects.requireNonNull(task, "task");
		if (!plannedTasks.contains(task)) {
			plannedTasks.add(task);
		}
	}

	public void unplanTask(Task task) {
		plannedTasks.remove(task);
	}

	public void startSprint() {
		if (this.startDate == null) {
			this.startDate = new Date();
		}
	}

	public void endSprint() {
		this.endDate = new Date();
	}
}
