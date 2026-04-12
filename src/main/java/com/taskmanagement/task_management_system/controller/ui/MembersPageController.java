package com.taskmanagement.task_management_system.controller.ui;

import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.repository.TeamMemberRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MembersPageController {

    private final TeamMemberRepository teamMemberRepository;

    public MembersPageController(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    @GetMapping
    public String list(Model model) {
        List<TeamMember> members = teamMemberRepository.findAll().stream()
                .sorted(Comparator.comparingInt(TeamMember::getUserId))
                .toList();
        model.addAttribute("members", members);
        return "members";
    }
}
