package com.taskmanagement.task_management_system.model;

public class Dependency {

    private int dependencyId;
    private String dependencyType;   // e.g. "FINISH_TO_START"

    private Task prerequisiteTask;   // this task must complete first
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