package com.taskmanagement.task_management_system.controller.ui.auth;

import com.taskmanagement.task_management_system.model.Role;

public record SessionUser(int userId, String name, Role role) {
}
