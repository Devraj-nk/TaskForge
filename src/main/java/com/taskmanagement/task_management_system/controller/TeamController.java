package com.taskmanagement.task_management_system.controller;

import com.taskmanagement.task_management_system.controller.dto.TeamDtos;
import com.taskmanagement.task_management_system.model.Team;
import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.service.TeamService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

	private final TeamService teamService;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@GetMapping
	public List<TeamDtos.TeamSummary> listTeams() {
		return teamService.listTeams().stream()
				.map(t -> new TeamDtos.TeamSummary(t.getTeamId(), t.getTeamName()))
				.toList();
	}

	@GetMapping("/{teamId}")
	public TeamDtos.TeamDetails getTeam(@PathVariable int teamId) {
		Team team = teamService.getTeam(teamId);
		List<TeamDtos.TeamMemberSummary> members = team.getMembers().stream()
				.map(m -> new TeamDtos.TeamMemberSummary(m.getUserId(), m.getName(), m.getEmail()))
				.toList();
		return new TeamDtos.TeamDetails(team.getTeamId(), team.getTeamName(), members);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TeamDtos.TeamSummary createTeam(@RequestBody TeamDtos.CreateTeamRequest request) {
		Team team = teamService.createTeam(request.name());
		return new TeamDtos.TeamSummary(team.getTeamId(), team.getTeamName());
	}

	@PostMapping("/{teamId}/members")
	@ResponseStatus(HttpStatus.CREATED)
	public TeamDtos.TeamMemberSummary addMember(@PathVariable int teamId, @RequestBody TeamDtos.AddMemberRequest request) {
		TeamMember member = teamService.addMember(teamId, request.name(), request.email(), request.password());
		return new TeamDtos.TeamMemberSummary(member.getUserId(), member.getName(), member.getEmail());
	}
}
