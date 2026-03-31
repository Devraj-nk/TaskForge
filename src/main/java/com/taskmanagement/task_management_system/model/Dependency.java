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

@Entity
@Table(name = "dependency")
public class Dependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dependency_id")
    private int dependencyId;

    @Column(name = "dependency_type")
    private String dependencyType;   // e.g. "FINISH_TO_START"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_task_id")
    private Task prerequisiteTask;   // this task must complete first

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task dependentTask;      // this task is blocked until above is done

    public Dependency() {}

    public Dependency(int dependencyId, String dependencyType,
                      Task prerequisiteTask, Task dependentTask) {
        this.dependencyId = dependencyId;
        this.dependencyType = dependencyType;
        this.prerequisiteTask = prerequisiteTask;
        this.dependentTask = dependentTask;
    }

    // --- Getters & Setters ---

    public int getDependencyId() { return dependencyId; }
    public void setDependencyId(int dependencyId) { this.dependencyId = dependencyId; }

    public String getDependencyType() { return dependencyType; }
    public void setDependencyType(String dependencyType) { this.dependencyType = dependencyType; }

    public Task getPrerequisiteTask() { return prerequisiteTask; }
    public void setPrerequisiteTask(Task prerequisiteTask) { this.prerequisiteTask = prerequisiteTask; }

    public Task getDependentTask() { return dependentTask; }
    public void setDependentTask(Task dependentTask) { this.dependentTask = dependentTask; }

    // --- UML Method ---

    /**
     * Returns true if the prerequisite task is COMPLETED.
     * This is what unblocks the dependent task.
     */
    public boolean isSatisfied() {
        if (prerequisiteTask == null) return true;
        return prerequisiteTask.getStatus() == TaskStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "Dependency{id=" + dependencyId
                + ", type='" + dependencyType + "'"
                + ", prerequisite=" + (prerequisiteTask != null ? prerequisiteTask.getTaskId() : "none")
                + "}";
    }
}