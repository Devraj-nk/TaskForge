package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Task {

    private int taskId;
    private String title;
    private String description;
    private int priority;          // 1 = HIGH, 2 = MEDIUM, 3 = LOW
    private TaskStatus status;
    private TeamMember assignedTo;

    private final List<SubTask> subTasks = new ArrayList<>();
    private final List<Dependency> dependencies = new ArrayList<>();

    public Task() {
        this.status = TaskStatus.CREATED;
    }

    public Task(int taskId, String title, String description, int priority) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TaskStatus.CREATED;
    }

    // --- Getters & Setters ---

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPriority() { return priority; }

    public TaskStatus getStatus() { return status; }

    public TeamMember getAssignedTo() { return assignedTo; }
    public void setAssignedTo(TeamMember member) {
        this.assignedTo = member;
        if (this.status == TaskStatus.CREATED && member != null) {
            this.status = TaskStatus.ASSIGNED;
        }
    }

    public List<SubTask> getSubTasks() {
        return Collections.unmodifiableList(subTasks);
    }

    public void addSubTask(SubTask subTask) {
        Objects.requireNonNull(subTask, "subTask cannot be null");
        subTasks.add(subTask);
    }

    public List<Dependency> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public void addDependency(Dependency dependency) {
        Objects.requireNonNull(dependency, "dependency cannot be null");
        dependencies.add(dependency);
    }

    // --- UML Methods ---

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void updateStatus(TaskStatus newStatus) {
        // A task cannot move to IN_PROGRESS if any dependency is unsatisfied
        if (newStatus == TaskStatus.IN_PROGRESS) {
            for (Dependency dep : dependencies) {
                if (!dep.isSatisfied()) {
                    this.status = TaskStatus.BLOCKED;
                    System.out.println("Task " + taskId + " is BLOCKED — unmet dependencies.");
                    return;
                }
            }
        }
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return "Task{id=" + taskId + ", title='" + title + "', status=" + status + ", priority=" + priority + "}";
    }
}