package com.taskmanagement.task_management_system.controller.ui;

import com.taskmanagement.task_management_system.model.Team;
import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teams")
public class TeamPageController {

    private final TeamService teamService;

    public TeamPageController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public String list(Model model) {
        return "redirect:/projects";
    }

    @PostMapping
    public String create(@RequestParam String name, RedirectAttributes redirectAttributes) {
        return "redirect:/projects";
    }

    @GetMapping("/{teamId}")
    public String details(@PathVariable int teamId, Model model) {
        return "redirect:/projects";
    }

    @PostMapping("/{teamId}/members")
    public String addMember(
            @PathVariable int teamId,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            RedirectAttributes redirectAttributes
    ) {
        return "redirect:/projects";
    }
}
