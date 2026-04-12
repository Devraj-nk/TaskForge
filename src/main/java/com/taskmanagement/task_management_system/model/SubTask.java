package com.taskmanagement.task_management_system.model;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "sub_task")
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id")
    private int subTaskId;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task parentTask;

    public SubTask() {
        this.status = TaskStatus.CREATED;
    }

    public SubTask(int subTaskId, String title, Task parentTask) {
        this.subTaskId = subTaskId;
        this.title = title;
        this.parentTask = parentTask;
        this.status = TaskStatus.CREATED;
    }

    // --- Builder Pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Integer subTaskId;
        private String title;
        private TaskStatus status;
        private Task parentTask;

        private Builder() {
        }

        public Builder subTaskId(int subTaskId) {
            this.subTaskId = subTaskId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder parentTask(Task parentTask) {
            this.parentTask = parentTask;
            return this;
        }

        public SubTask build() {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("SubTask title is required");
            }
            if (parentTask == null) {
                throw new IllegalArgumentException("SubTask parentTask is required");
            }

            SubTask subTask = new SubTask();
            if (subTaskId != null) {
                subTask.subTaskId = subTaskId;
            }
            subTask.title = title.trim();
            subTask.parentTask = parentTask;
            subTask.status = (status != null ? status : TaskStatus.CREATED);
            return subTask;
        }
    }

    // --- Getters & Setters ---

    public int getSubTaskId() { return subTaskId; }
    public void setSubTaskId(int subTaskId) { this.subTaskId = subTaskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TaskStatus getStatus() { return status; }

    public Task getParentTask() { return parentTask; }
    public void setParentTask(Task parentTask) { this.parentTask = parentTask; }

    // --- UML Method ---

    public void updateStatus(TaskStatus newStatus) {
        this.status = newStatus;

        // After updating, check if ALL subtasks of parent are done
        // If yes, auto-complete the parent Task too
        if (newStatus == TaskStatus.COMPLETED && parentTask != null) {
            boolean allDone = parentTask.getSubTasks()
                    .stream()
                    .allMatch(st -> st.getStatus() == TaskStatus.COMPLETED);

            if (allDone) {
                parentTask.updateStatus(TaskStatus.COMPLETED);
                System.out.println("All subtasks done! Task '"
                        + parentTask.getTitle() + "' auto-completed.");
            }
        }
    }

    @Override
    public String toString() {
        return "SubTask{id=" + subTaskId + ", title='" + title + "', status=" + status + "}";
    }
}
