package com.taskmanagement.task_management_system.controller.ui;

import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.model.TaskStatus;
import com.taskmanagement.task_management_system.service.ProjectService;
import com.taskmanagement.task_management_system.service.TaskService;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
public class TaskPageController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskPageController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskService.listTasks());
        model.addAttribute("projects", projectService.listProjects());
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks";
    }

    @GetMapping("/{taskId}")
    public String details(@PathVariable int taskId, Model model) {
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        return "task-details";
    }

    @PostMapping("/{taskId}/status")
    public String updateStatus(
            @PathVariable int taskId,
            @RequestParam String status,
            RedirectAttributes redirectAttributes
    ) {
        TaskStatus newStatus = TaskStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        taskService.updateTask(taskId, null, null, null, newStatus);
        redirectAttributes.addFlashAttribute("message", "Status updated");
        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/{taskId}/assign")
    public String assign(
            @PathVariable int taskId,
            @RequestParam int teamMemberId,
            RedirectAttributes redirectAttributes
    ) {
        taskService.assignTask(taskId, teamMemberId);
        redirectAttributes.addFlashAttribute("message", "Task assigned");
        return "redirect:/tasks/" + taskId;
    }
}
