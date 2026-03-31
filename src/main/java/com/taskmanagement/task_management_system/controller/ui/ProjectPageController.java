package com.taskmanagement.task_management_system.controller.ui;

import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Sprint;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.service.ProjectService;
import com.taskmanagement.task_management_system.service.SprintService;
import com.taskmanagement.task_management_system.service.TaskService;
import com.taskmanagement.task_management_system.service.TeamService;
import com.taskmanagement.task_management_system.service.DependencyService;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectPageController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final TeamService teamService;
    private final SprintService sprintService;
    private final DependencyService dependencyService;

    public ProjectPageController(
            ProjectService projectService,
            TaskService taskService,
            TeamService teamService,
            SprintService sprintService,
            DependencyService dependencyService
    ) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.teamService = teamService;
        this.sprintService = sprintService;
        this.dependencyService = dependencyService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.listProjects());
        return "projects";
    }

    @PostMapping
    public String create(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            RedirectAttributes redirectAttributes
    ) {
        Project project = projectService.createProject(name, description);
        redirectAttributes.addFlashAttribute("message", "Project created: " + project.getName());
        return "redirect:/projects/" + project.getProjectId();
    }

    @GetMapping("/{projectId}")
    public String details(@PathVariable int projectId, Model model) {
        Project project = projectService.getProject(projectId);
        model.addAttribute("project", project);
        model.addAttribute("tasks", project.getTasks());
        model.addAttribute("sprints", project.getSprints());
        model.addAttribute("teams", teamService.listTeams());
        return "project-details";
    }

    @PostMapping("/{projectId}/team")
    public String createAndAssignTeam(
            @PathVariable int projectId,
            @RequestParam String teamName,
            RedirectAttributes redirectAttributes
    ) {
        projectService.createAndAssignTeam(projectId, teamName);
        redirectAttributes.addFlashAttribute("message", "Team created and assigned");
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/team/assign")
    public String assignExistingTeam(
            @PathVariable int projectId,
            @RequestParam int teamId,
            RedirectAttributes redirectAttributes
    ) {
        projectService.assignTeam(projectId, teamId);
        redirectAttributes.addFlashAttribute("message", "Team assigned");
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/sprints")
    public String createSprint(
            @PathVariable int projectId,
            RedirectAttributes redirectAttributes
    ) {
        Sprint sprint = projectService.createSprint(projectId, null, null);
        redirectAttributes.addFlashAttribute("message", "Sprint created: " + sprint.getSprintId());
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/sprints/{sprintId}/start")
    public String startSprint(
            @PathVariable int projectId,
            @PathVariable int sprintId,
            RedirectAttributes redirectAttributes
    ) {
        sprintService.startSprint(sprintId);
        redirectAttributes.addFlashAttribute("message", "Sprint started");
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/tasks")
    public String createTask(
            @PathVariable int projectId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam int priority,
            RedirectAttributes redirectAttributes
    ) {
        taskService.createTask(projectId, title, description, priority);
        redirectAttributes.addFlashAttribute("message", "Task created");
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/dependencies")
    public String defineDependency(
            @PathVariable int projectId,
            @RequestParam int prerequisiteTaskId,
            @RequestParam int dependentTaskId,
            @RequestParam(required = false) String dependencyType,
            RedirectAttributes redirectAttributes
    ) {
        Task prerequisite = taskService.getTask(prerequisiteTaskId);
        Task dependent = taskService.getTask(dependentTaskId);
        if (prerequisite.getProject() == null || dependent.getProject() == null
                || prerequisite.getProject().getProjectId() != projectId
                || dependent.getProject().getProjectId() != projectId) {
            redirectAttributes.addFlashAttribute("message", "Both tasks must belong to this project");
            return "redirect:/projects/" + projectId;
        }

        dependencyService.createDependency(prerequisiteTaskId, dependentTaskId, dependencyType);
        redirectAttributes.addFlashAttribute("message", "Dependency created");
        return "redirect:/projects/" + projectId;
    }
}
