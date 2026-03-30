
package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamMember extends User {

    private List<Task> assignedTasks = new ArrayList<>();

    public TeamMember() {}

    public TeamMember(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    // --- Getters ---

    public List<Task> getAssignedTasks() {
        return Collections.unmodifiableList(assignedTasks);
    }

    public void addAssignedTask(Task task) {
        if (task != null && !assignedTasks.contains(task)) {
            assignedTasks.add(task);
        }
    }

    // --- UML Methods ---

    /**
     * Prints all tasks assigned to this member.
     */
    public void viewTasks() {
        if (assignedTasks.isEmpty()) {
            System.out.println(getName() + " has no assigned tasks.");
            return;
        }
        System.out.println("Tasks assigned to " + getName() + ":");
        for (Task task : assignedTasks) {
            System.out.println("  " + task);
        }
    }

    /**
     * TeamMember can only update status of their OWN assigned tasks.
     */
    public void updateTaskStatus(Task task, TaskStatus newStatus) {
        if (!assignedTasks.contains(task)) {
            System.out.println("Error: " + getName() 
                + " is not assigned to task '" + task.getTitle() + "'");
            return;
        }
        task.updateStatus(newStatus);
        System.out.println(getName() + " updated task '" 
            + task.getTitle() + "' to " + newStatus);
    }

    /**
     * Logs work hours on a task.
     */
    public void logWork(Task task, int hoursSpent) {
        if (!assignedTasks.contains(task)) {
            System.out.println("Error: " + getName() 
                + " cannot log work on unassigned task.");
            return;
        }
        System.out.println(getName() + " logged " + hoursSpent 
            + " hour(s) on task '" + task.getTitle() + "'");
    }

    @Override
    public String toString() {
        return "TeamMember{id=" + getUserId() + ", name='" + getName() + "'}";
    }
}