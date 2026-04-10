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
/**
 * **Design patterns used:**
 * - Domain Model (JPA Entity): this class is a persistence-backed domain entity.
 * - Builder: `Sprint.builder()...build()` provides controlled object creation.
 *
 * **GRASP:**
 * - Information Expert: owns `plannedTasks` and manages sprint lifecycle (`startSprint`, `endSprint`).
 *
 * **SOLID:**
 * - SRP: represents Sprint state + lifecycle rules.
 * - Encapsulation (supports maintainability): exposes unmodifiable views of internal collections.
 *
 * **Related builder usage (planning):** `SprintPlanner.builder()` is used for task selection and is wired via `PlanningConfig`.
 */
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

    /**
     * Builder pattern entry-point.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Integer sprintId;
        private Date startDate;
        private Date endDate;
        private Project project;

        private Builder() {
        }

        public Builder sprintId(int sprintId) {
            this.sprintId = sprintId;
            return this;
        }

        public Builder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder project(Project project) {
            this.project = project;
            return this;
        }

        public Sprint build() {
            Sprint sprint = new Sprint();
            if (sprintId != null) {
                sprint.setSprintId(sprintId);
            }
            sprint.setStartDate(startDate);
            sprint.setEndDate(endDate);
            sprint.setProject(project);
            return sprint;
        }
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
        // GRASP (Low Coupling) + SOLID (Encapsulation): callers can't mutate the internal list directly.
        return Collections.unmodifiableList(plannedTasks);
    }

    public void planTask(Task task) {
        // GRASP (Information Expert): Sprint decides which tasks are planned for this sprint.
        Objects.requireNonNull(task, "task");
        if (!plannedTasks.contains(task)) {
            plannedTasks.add(task);
        }
    }

    public void unplanTask(Task task) {
        // GRASP (Information Expert): Sprint manages its planned tasks list.
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