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

    private Project project;
    private final List<Task> plannedTasks = new ArrayList<>();

    public Sprint() {}

    public Sprint(int sprintId) {
        this.sprintId = sprintId;
    }

    public Sprint(int sprintId, Date startDate, Date endDate) {
        this.sprintId = sprintId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // --- Getters & Setters --- (all kept from your version)

    public int getSprintId() { return sprintId; }
    public void setSprintId(int sprintId) { this.sprintId = sprintId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

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

    // --- UML Methods ---

    public void startSprint() {
        if (this.startDate == null) {
            this.startDate = new Date();
        }
        // ✅ Added: print confirmation
        System.out.println("Sprint " + sprintId + " started on " + startDate);
    }

    public void endSprint() {
        // ✅ Added: check if sprint was started first
        if (this.startDate == null) {
            System.out.println("Error: Sprint " + sprintId 
                + " hasn't been started yet.");
            return;
        }
        this.endDate = new Date();

        // ✅ Added: report task completion summary
        long completed = plannedTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .count();
        System.out.println("Sprint " + sprintId + " ended. "
                + completed + "/" + plannedTasks.size() 
                + " tasks completed.");
    }

    // ✅ Added: toString
    @Override
    public String toString() {
        return "Sprint{id=" + sprintId 
            + ", tasks=" + plannedTasks.size() 
            + ", start=" + startDate 
            + ", end=" + endDate + "}";
    }
}