package com.taskmanagement.task_management_system.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.taskmanagement.task_management_system.model.User;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "priority", nullable = false)
    private int priority;          // 1 = HIGH, 2 = MEDIUM, 3 = LOW

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTask> subTasks = new ArrayList<>();

    @OneToMany(mappedBy = "dependentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dependency> dependencies = new ArrayList<>();

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TaskStatus getStatus() { return status; }

    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User member) {
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