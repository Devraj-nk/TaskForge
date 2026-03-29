package com.taskmanagement.task_management_system.view;

import com.taskmanagement.task_management_system.model.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private Scanner scanner = new Scanner(System.in);

    // In-memory storage for this session
    private List<User> users = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();

    private User loggedInUser = null;

    // ID counters
    private int projectCounter = 1;
    private int taskCounter = 1;
    private int sprintCounter = 1;
    private int dependencyCounter = 1;
    private int userCounter = 1;

    // =====================
    //   ENTRY POINT
    // =====================

    public void start() {
        System.out.println("==============================");
        System.out.println("   Welcome to TaskForge!      ");
        System.out.println("==============================");

        // Seed some default users for testing
        seedData();

        boolean running = true;
        while (running) {
            if (loggedInUser == null) {
                running = showMainMenu();
            } else if (loggedInUser instanceof Admin) {
                showAdminMenu();
            } else if (loggedInUser instanceof ProjectManager) {
                showProjectManagerMenu();
            } else if (loggedInUser instanceof TeamMember) {
                showTeamMemberMenu();
            }
        }
        System.out.println("Goodbye!");
    }

    // =====================
    //   MAIN MENU
    // =====================

    private boolean showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Choice: ");

        int choice = readInt();
        switch (choice) {
            case 1: login(); return true;
            case 2: return false;
            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }

    private void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        for (User user : users) {
            if (user.login(email, password)) {
                loggedInUser = user;
                System.out.println("\nWelcome, " + user.getName() + "!");
                return;
            }
        }
        System.out.println("Invalid credentials. Try again.");
    }

    // =====================
    //   ADMIN MENU
    // =====================

    private void showAdminMenu() {
        Admin admin = (Admin) loggedInUser;
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Add User");
        System.out.println("2. View All Users");
        System.out.println("3. Logout");
        System.out.print("Choice: ");

        switch (readInt()) {
            case 1: addUser(admin); break;
            case 2: viewAllUsers(); break;
            case 3: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void addUser(Admin admin) {
        System.out.println("\nSelect role:");
        System.out.println("1. Project Manager");
        System.out.println("2. Team Member");
        System.out.print("Choice: ");
        int roleChoice = readInt();

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User newUser;
        if (roleChoice == 1) {
            newUser = new ProjectManager(userCounter++, name, email, password);
        } else {
            newUser = new TeamMember(userCounter++, name, email, password);
        }

        users.add(newUser);
        admin.manageUsers("ADD", newUser);
    }

    private void viewAllUsers() {
        System.out.println("\n--- All Users ---");
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (User u : users) {
            System.out.println(u);
        }
    }

    // =====================
    //   PROJECT MANAGER MENU
    // =====================

    private void showProjectManagerMenu() {
        ProjectManager pm = (ProjectManager) loggedInUser;
        System.out.println("\n--- Project Manager Menu ---");
        System.out.println("1. Create Project");
        System.out.println("2. Create Sprint");
        System.out.println("3. Create Task");
        System.out.println("4. Assign Task to Member");
        System.out.println("5. Define Dependency");
        System.out.println("6. View Projects");
        System.out.println("7. Logout");
        System.out.print("Choice: ");

        switch (readInt()) {
            case 1: createProject(pm); break;
            case 2: createSprint(pm); break;
            case 3: createTask(pm); break;
            case 4: assignTask(pm); break;
            case 5: defineDependency(pm); break;
            case 6: viewProjects(pm); break;
            case 7: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void createProject(ProjectManager pm) {
        System.out.print("Project name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();

        Project p = pm.createProject(projectCounter++, name, desc);
        projects.add(p);
    }

    private void createSprint(ProjectManager pm) {
        if (pm.getManagedProjects().isEmpty()) {
            System.out.println("No projects found. Create a project first.");
            return;
        }
        System.out.println("Select project:");
        listProjects(pm.getManagedProjects());
        int idx = readInt() - 1;
        if (idx < 0 || idx >= pm.getManagedProjects().size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Project selected = pm.getManagedProjects().get(idx);
        pm.createSprint(sprintCounter++, new Date(), null, selected);
    }

    private void createTask(ProjectManager pm) {
        if (pm.getManagedProjects().isEmpty()) {
            System.out.println("No projects found. Create a project first.");
            return;
        }
        System.out.print("Task title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Priority (1=HIGH, 2=MEDIUM, 3=LOW): ");
        int priority = readInt();

        System.out.println("Select project to add task to:");
        listProjects(pm.getManagedProjects());
        int idx = readInt() - 1;
        if (idx < 0 || idx >= pm.getManagedProjects().size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Task task = new Task(taskCounter++, title, desc, priority);
        pm.getManagedProjects().get(idx).addTask(task);
        tasks.add(task);
        System.out.println("Task '" + title + "' created.");
    }

    private void assignTask(ProjectManager pm) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found. Create a task first.");
            return;
        }

        // Pick task
        System.out.println("Select task:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        int tIdx = readInt() - 1;
        if (tIdx < 0 || tIdx >= tasks.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        // Pick team member
        List<TeamMember> members = getTeamMembers();
        if (members.isEmpty()) {
            System.out.println("No team members found.");
            return;
        }
        System.out.println("Select team member:");
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i));
        }
        int mIdx = readInt() - 1;
        if (mIdx < 0 || mIdx >= members.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        pm.assignTask(tasks.get(tIdx), members.get(mIdx));
    }

    private void defineDependency(ProjectManager pm) {
        if (tasks.size() < 2) {
            System.out.println("Need at least 2 tasks to define a dependency.");
            return;
        }

        System.out.println("Select PREREQUISITE task (must finish first):");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        int preIdx = readInt() - 1;

        System.out.println("Select DEPENDENT task (blocked until above is done):");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        int depIdx = readInt() - 1;

        if (preIdx == depIdx) {
            System.out.println("A task cannot depend on itself.");
            return;
        }
        if (preIdx < 0 || depIdx < 0 
            || preIdx >= tasks.size() || depIdx >= tasks.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        pm.defineDependencies(dependencyCounter++,
                tasks.get(preIdx),
                tasks.get(depIdx),
                "FINISH_TO_START");
    }

    private void viewProjects(ProjectManager pm) {
        if (pm.getManagedProjects().isEmpty()) {
            System.out.println("No projects yet.");
            return;
        }
        for (Project p : pm.getManagedProjects()) {
            System.out.println("\n" + p);
            System.out.println("  Tasks:");
            for (Task t : p.getTasks()) {
                System.out.println("    " + t);
            }
        }
    }

    // =====================
    //   TEAM MEMBER MENU
    // =====================

    private void showTeamMemberMenu() {
        TeamMember member = (TeamMember) loggedInUser;
        System.out.println("\n--- Team Member Menu ---");
        System.out.println("1. View My Tasks");
        System.out.println("2. Update Task Status");
        System.out.println("3. Log Work");
        System.out.println("4. Logout");
        System.out.print("Choice: ");

        switch (readInt()) {
            case 1: member.viewTasks(); break;
            case 2: updateTaskStatus(member); break;
            case 3: logWork(member); break;
            case 4: logout(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void updateTaskStatus(TeamMember member) {
        List<Task> myTasks = member.getAssignedTasks();
        if (myTasks.isEmpty()) {
            System.out.println("No tasks assigned to you.");
            return;
        }

        System.out.println("Select task to update:");
        for (int i = 0; i < myTasks.size(); i++) {
            System.out.println((i + 1) + ". " + myTasks.get(i));
        }
        int idx = readInt() - 1;
        if (idx < 0 || idx >= myTasks.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.println("Select new status:");
        System.out.println("1. IN_PROGRESS");
        System.out.println("2. COMPLETED");
        System.out.println("3. BLOCKED");
        int statusChoice = readInt();

        TaskStatus newStatus;
        switch (statusChoice) {
            case 1: newStatus = TaskStatus.IN_PROGRESS; break;
            case 2: newStatus = TaskStatus.COMPLETED; break;
            case 3: newStatus = TaskStatus.BLOCKED; break;
            default:
                System.out.println("Invalid status.");
                return;
        }
        member.updateTaskStatus(myTasks.get(idx), newStatus);
    }

    private void logWork(TeamMember member) {
        List<Task> myTasks = member.getAssignedTasks();
        if (myTasks.isEmpty()) {
            System.out.println("No tasks assigned to you.");
            return;
        }

        System.out.println("Select task to log work on:");
        for (int i = 0; i < myTasks.size(); i++) {
            System.out.println((i + 1) + ". " + myTasks.get(i));
        }
        int idx = readInt() - 1;
        if (idx < 0 || idx >= myTasks.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Hours spent: ");
        int hours = readInt();
        member.logWork(myTasks.get(idx), hours);
    }

    // =====================
    //   HELPERS
    // =====================

    private void logout() {
        System.out.println(loggedInUser.getName() + " logged out.");
        loggedInUser.logout();
        loggedInUser = null;
    }

    private void listProjects(List<Project> projectList) {
        for (int i = 0; i < projectList.size(); i++) {
            System.out.println((i + 1) + ". " + projectList.get(i).getName());
        }
    }

    private List<TeamMember> getTeamMembers() {
        List<TeamMember> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof TeamMember) {
                result.add((TeamMember) u);
            }
        }
        return result;
    }

    private int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // =====================
    //   SEED DATA
    // =====================

    private void seedData() {
        // Create default users for testing
        Admin admin = new Admin(userCounter++, "Admin", "admin@test.com", "admin123");
        ProjectManager pm = new ProjectManager(userCounter++, "Alice", "alice@test.com", "alice123");
        TeamMember tm1 = new TeamMember(userCounter++, "Bob", "bob@test.com", "bob123");
        TeamMember tm2 = new TeamMember(userCounter++, "Charlie", "charlie@test.com", "charlie123");

        users.add(admin);
        users.add(pm);
        users.add(tm1);
        users.add(tm2);

        System.out.println("--- Default accounts created ---");
        System.out.println("Admin    → admin@test.com   / admin123");
        System.out.println("PM       → alice@test.com   / alice123");
        System.out.println("Member 1 → bob@test.com     / bob123");
        System.out.println("Member 2 → charlie@test.com / charlie123");
        System.out.println("--------------------------------");
    }
}