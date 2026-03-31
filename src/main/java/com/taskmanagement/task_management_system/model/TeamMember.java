package com.taskmanagement.task_management_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "team_member")
public class TeamMember extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> assignedTasks = new ArrayList<>();

    public TeamMember() {}

    public TeamMember(int userId, String name, String email, String password) {
        super(userId, name, email, password);
        setRole(Role.TEAM_MEMBER);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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