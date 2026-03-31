package com.taskmanagement.task_management_system.controller.ui;

import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.service.ProjectService;
import com.taskmanagement.task_management_system.service.TaskService;
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

    public ProjectPageController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
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
        return "project-details";
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
}
