package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Admin extends User {

    private List<User> systemUsers = new ArrayList<>();

    public Admin() { super(); }

    public Admin(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public List<User> getSystemUsers() {
        return Collections.unmodifiableList(systemUsers);
    }

    // --- UML Methods ---

    /**
     * Admin can add or remove users from the system.
     */
    public void manageUsers(String action, User user) {
        Objects.requireNonNull(user, "user cannot be null");

        switch (action.toUpperCase()) {
            case "ADD":
                if (!systemUsers.contains(user)) {
                    systemUsers.add(user);
                    System.out.println("User '" + user.getName() 
                        + "' added to system by Admin " + getName());
                } else {
                    System.out.println("User '" + user.getName() 
                        + "' already exists in system.");
                }
                break;

            case "REMOVE":
                if (systemUsers.remove(user)) {
                    System.out.println("User '" + user.getName() 
                        + "' removed from system by Admin " + getName());
                } else {
                    System.out.println("User '" + user.getName() 
                        + "' not found in system.");
                }
                break;

            default:
                System.out.println("Unknown action: " + action 
                    + ". Use 'ADD' or 'REMOVE'.");
        }
    }

    /**
     * Admin can assign roles by returning the correct User subtype.
     * Prints the current role of any user in the system.
     */
    public void manageRoles(User user) {
        Objects.requireNonNull(user, "user cannot be null");

        String role;
        if (user instanceof ProjectManager) {
            role = "Project Manager";
        } else if (user instanceof TeamMember) {
            role = "Team Member";
        } else if (user instanceof Admin) {
            role = "Admin";
        } else {
            role = "Unknown Role";
        }

        System.out.println("User '" + user.getName() 
            + "' has role: " + role);
    }

    @Override
    public String toString() {
        return "Admin{id=" + getUserId() + ", name='" + getName() + "'}";
    }
}