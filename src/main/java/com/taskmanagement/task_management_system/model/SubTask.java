package com.taskmanagement.task_management_system.model;

public class SubTask {

    private int subTaskId;
    private String title;
    private TaskStatus status;
    private Task parentTask;        // composition — SubTask belongs to a Task

    public SubTask() {
        this.status = TaskStatus.CREATED;
    }

    public SubTask(int subTaskId, String title, Task parentTask) {
        this.subTaskId = subTaskId;
        this.title = title;
        this.parentTask = parentTask;
        this.status = TaskStatus.CREATED;
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
