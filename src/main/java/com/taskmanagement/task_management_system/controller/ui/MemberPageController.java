package com.taskmanagement.task_management_system.controller.ui;

import com.taskmanagement.task_management_system.controller.ui.auth.SessionConstants;
import com.taskmanagement.task_management_system.controller.ui.auth.SessionUser;
import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.model.Sprint;
import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.model.Task;
import com.taskmanagement.task_management_system.model.TaskStatus;
import com.taskmanagement.task_management_system.repository.SprintRepository;
import com.taskmanagement.task_management_system.repository.TeamMemberRepository;
import com.taskmanagement.task_management_system.service.ProjectService;
import com.taskmanagement.task_management_system.service.TaskService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
public class MemberPageController {

    private final TeamMemberRepository teamMemberRepository;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final SprintRepository sprintRepository;

        public MemberPageController(
                        TeamMemberRepository teamMemberRepository,
                        TaskService taskService,
                        ProjectService projectService,
                        SprintRepository sprintRepository
        ) {
        this.teamMemberRepository = teamMemberRepository;
        this.taskService = taskService;
        this.projectService = projectService;
		this.sprintRepository = sprintRepository;
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        Object value = session.getAttribute(SessionConstants.SESSION_USER);
        if (!(value instanceof SessionUser sessionUser)) {
            return "redirect:/login";
        }

        TeamMember member = teamMemberRepository.findById(sessionUser.userId()).orElse(null);
        model.addAttribute("member", member);

        Project project = null;
        if (member != null && member.getTeam() != null && member.getTeam().getProject() != null) {
            int projectId = member.getTeam().getProject().getProjectId();
            project = projectService.getProject(projectId);
        }
        model.addAttribute("project", project);

        Sprint currentSprint = null;
        if (project != null) {
            List<Sprint> sprints = sprintRepository.findByProject_ProjectIdOrderBySprintIdDesc(project.getProjectId());
            currentSprint = sprints.stream()
                    .filter(s -> s.getEndDate() == null)
                    .sorted(Comparator.comparingInt(Sprint::getSprintId).reversed())
                    .findFirst()
                    .orElseGet(() -> sprints.isEmpty() ? null : sprints.get(0));
        }
        model.addAttribute("currentSprint", currentSprint);

        List<Task> myTasks = taskService.listTasksAssignedTo(sessionUser.userId());
        model.addAttribute("myTasks", myTasks);
        model.addAttribute("blockersByTaskId", blockersByTaskId(myTasks));

        return "member-dashboard";
    }

    @GetMapping("/sprints/{sprintId}")
    public String sprintDetails(
            @PathVariable int sprintId,
            HttpSession session,
            Model model
    ) {
        Object value = session.getAttribute(SessionConstants.SESSION_USER);
        if (!(value instanceof SessionUser sessionUser)) {
            return "redirect:/login";
        }

        TeamMember member = teamMemberRepository.findById(sessionUser.userId()).orElse(null);
        if (member == null || member.getTeam() == null || member.getTeam().getProject() == null) {
            model.addAttribute("member", member);
            model.addAttribute("project", null);
            model.addAttribute("sprint", null);
            model.addAttribute("tasks", List.of());
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("blockersByTaskId", Map.of());
            return "member-sprint";
        }

        int projectId = member.getTeam().getProject().getProjectId();
        Project project = projectService.getProject(projectId);
        Sprint sprint = sprintRepository.findById(sprintId).orElse(null);
        if (sprint != null && (sprint.getProject() == null || sprint.getProject().getProjectId() != projectId)) {
            sprint = null;
        }

        List<Task> tasks = project != null ? new ArrayList<>(project.getTasks()) : List.of();
        model.addAttribute("member", member);
        model.addAttribute("project", project);
        model.addAttribute("sprint", sprint);
        model.addAttribute("tasks", tasks);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("blockersByTaskId", blockersByTaskId(tasks));
        return "member-sprint";
    }

    @PostMapping("/tasks/{taskId}/status")
    public String updateMyTaskStatus(
            @PathVariable int taskId,
            @RequestParam String status,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Object value = session.getAttribute(SessionConstants.SESSION_USER);
        if (!(value instanceof SessionUser sessionUser)) {
            return "redirect:/login";
        }

        Task task = taskService.getTask(taskId);
        if (task.getAssignedTo() == null || task.getAssignedTo().getUserId() != sessionUser.userId()) {
            redirectAttributes.addFlashAttribute("message", "You can only update your own assigned tasks");
            return "redirect:/member";
        }

        TaskStatus newStatus = TaskStatus.valueOf(status.trim().toUpperCase());
        Task updated = taskService.updateTask(taskId, null, null, null, newStatus);
        if (newStatus == TaskStatus.IN_PROGRESS && updated.getStatus() == TaskStatus.BLOCKED) {
            redirectAttributes.addFlashAttribute("message", "Task is BLOCKED: complete prerequisites first");
        } else {
            redirectAttributes.addFlashAttribute("message", "Task status updated: " + updated.getStatus());
        }
        return "redirect:/member";
    }

    private Map<Integer, List<String>> blockersByTaskId(List<Task> tasks) {
        Objects.requireNonNull(tasks, "tasks");
        return tasks.stream().collect(Collectors.toMap(
                Task::getTaskId,
                task -> task.getDependencies().stream()
                        .filter(dep -> !dep.isSatisfied() && dep.getPrerequisiteTask() != null)
                        .map(dep -> {
                            Task prereq = dep.getPrerequisiteTask();
                            return "Task " + prereq.getTaskId() + ": " + prereq.getTitle() + " (" + prereq.getStatus() + ")";
                        })
                        .toList()
        ));
    }
}
