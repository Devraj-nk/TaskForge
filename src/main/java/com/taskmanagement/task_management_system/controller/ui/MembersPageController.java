package com.taskmanagement.task_management_system.controller.ui;

import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.repository.TeamMemberRepository;
import com.taskmanagement.task_management_system.service.ProjectService;
import com.taskmanagement.task_management_system.service.TeamService;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
public class MembersPageController {

    private final TeamMemberRepository teamMemberRepository;
    private final ProjectService projectService;
    private final TeamService teamService;

    public MembersPageController(
            TeamMemberRepository teamMemberRepository,
            ProjectService projectService,
            TeamService teamService
    ) {
        this.teamMemberRepository = teamMemberRepository;
        this.projectService = projectService;
        this.teamService = teamService;
    }

    @GetMapping
    public String list(Model model) {
        List<TeamMember> members = teamMemberRepository.findAll().stream()
                .sorted(Comparator.comparingInt(TeamMember::getUserId))
                .toList();
        model.addAttribute("members", members);
        model.addAttribute("projects", projectService.listProjects());
        return "members";
    }

    @PostMapping("/{memberId}/project")
    public String assignProject(
            @PathVariable int memberId,
            @RequestParam(required = false) String projectId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (projectId == null || projectId.isBlank()) {
                teamService.removeMemberFromTeam(memberId);
                redirectAttributes.addFlashAttribute("message", "Member removed from team");
                return "redirect:/members";
            }

            int pid = Integer.parseInt(projectId.trim());
            teamService.assignMemberToProject(memberId, pid);
            redirectAttributes.addFlashAttribute("message", "Member assigned to project/team");
            return "redirect:/members";
        } catch (NumberFormatException ex) {
            redirectAttributes.addFlashAttribute("message", "Invalid project id");
            return "redirect:/members";
        }
    }
}
