package com.taskmanagement.task_management_system.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class DependencyTest {

    @Test
    void isSatisfiedShouldDependOnPrerequisiteStatus() {
        Task prerequisite = new Task(1, "A", "Prerequisite", 1);
        Task dependent = new Task(2, "B", "Dependent", 1);
        Dependency dependency = new Dependency(10, "FINISH_TO_START", prerequisite, dependent);

        assertFalse(dependency.isSatisfied());

        prerequisite.updateStatus(TaskStatus.COMPLETED);
        assertTrue(dependency.isSatisfied());
    }

    @Test
    void isSatisfiedShouldBeTrueWhenNoPrerequisite() {
        Task dependent = new Task(2, "B", "Dependent", 1);
        Dependency dependency = new Dependency(10, "FINISH_TO_START", null, dependent);

        assertTrue(dependency.isSatisfied());
    }
}