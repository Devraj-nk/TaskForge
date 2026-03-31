package com.taskmanagement.task_management_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sprint")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private int sprintId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Transient
    private List<Task> plannedTasks = new ArrayList<>();

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