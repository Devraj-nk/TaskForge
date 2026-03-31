package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ProjectManager extends User {

    private final List<Project> managedProjects = new ArrayList<>();
    private Team team;

    public ProjectManager() { super(); }

    public ProjectManager(int userId, String name, String email, String password) {
        super(userId, name, email, password);
		setRole(Role.PROJECT_MANAGER);
    }

    public List<Project> getManagedProjects() {
        return Collections.unmodifiableList(managedProjects);
    }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    // --- UML Methods ---

    public Project createProject(int projectId, String name, String description) {
        Project project = new Project(projectId, name, description);
        project.setManager(this);
        if (this.team != null) {
            project.setTeam(this.team);
        }
        managedProjects.add(project);
        System.out.println("Project '" + name + "' created by " + getName());
        return project;
    }

    public Sprint createSprint(int sprintId, Date startDate,
                                Date endDate, Project project) {
        if (!managedProjects.contains(project)) {
            System.out.println("Error: " + getName()
                + " does not manage project '" + project.getName() + "'");
            return null;
        }
        Sprint sprint = new Sprint(sprintId, startDate, endDate);
        project.addSprint(sprint);
        System.out.println("Sprint " + sprintId + " created for '"
            + project.getName() + "'");
        return sprint;
    }

    public void assignTask(Task task, TeamMember member) {
        Objects.requireNonNull(task, "task");
        Objects.requireNonNull(member, "member");
        task.setAssignedTo(member);
        member.addAssignedTask(task);
        System.out.println("Task '" + task.getTitle()
            + "' assigned to " + member.getName());
    }

    public Dependency defineDependencies(int dependencyId,
                                          Task prerequisiteTask,
                                          Task dependentTask,
                                          String dependencyType) {
        Objects.requireNonNull(prerequisiteTask, "prerequisiteTask");
        Objects.requireNonNull(dependentTask, "dependentTask");
        Dependency dep = new Dependency(
            dependencyId, dependencyType, prerequisiteTask, dependentTask
        );
        dependentTask.addDependency(dep);
        System.out.println("Dependency: '" + prerequisiteTask.getTitle()
            + "' must finish before '" + dependentTask.getTitle() + "'");
        return dep;
    }

    // Helper — kept from your original
    public void manageProject(Project project) {
        Objects.requireNonNull(project, "project");
        if (!managedProjects.contains(project)) {
            managedProjects.add(project);
            project.setManager(this);
        }
    }

    @Override
    public String toString() {
        return "ProjectManager{id=" + getUserId() + ", name='" + getName() + "'}";
    }
}