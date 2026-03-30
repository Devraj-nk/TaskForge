package com.taskmanagement.task_management_system.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class TeamMemberTest {

    @Test
    void updateTaskStatusShouldWorkForAssignedTasks() {
        TeamMember member = new TeamMember(11, "Jordan", "jordan@mail.com", "pw");
        Task task = new Task(100, "Build API", "Implement endpoint", 1);

        member.addAssignedTask(task);
        member.updateTaskStatus(task, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void updateTaskStatusShouldNotChangeUnassignedTask() {
        TeamMember member = new TeamMember(11, "Jordan", "jordan@mail.com", "pw");
        Task task = new Task(100, "Build API", "Implement endpoint", 1);

        member.updateTaskStatus(task, TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.CREATED, task.getStatus());
    }

    @Test
    void assignedTasksListShouldBeUnmodifiableAndUnique() {
        TeamMember member = new TeamMember();
        Task task = new Task(1, "T", "D", 1);

        member.addAssignedTask(task);
        member.addAssignedTask(task);

        assertEquals(1, member.getAssignedTasks().size());
        assertThrows(UnsupportedOperationException.class,
                () -> member.getAssignedTasks().add(new Task()));
    }
}