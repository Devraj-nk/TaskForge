package com.taskmanagement.task_management_system.controller.dto;

public final class TaskDtos {
    private TaskDtos() {
    }

    public record CreateTaskRequest(String title, String description, int priority) {
    }

    public record UpdateTaskRequest(String title, String description, Integer priority, String status) {
    }

    public record AssignTaskRequest(int teamMemberId) {
    }
}
