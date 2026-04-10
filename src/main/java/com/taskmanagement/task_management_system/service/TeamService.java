package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Team;
import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.model.Role;
import com.taskmanagement.task_management_system.repository.TeamRepository;
import com.taskmanagement.task_management_system.repository.TeamMemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;

	public TeamService(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository) {
		this.teamRepository = teamRepository;
		this.teamMemberRepository = teamMemberRepository;
	}

	public List<Team> listTeams() {
		return teamRepository.findAll();
	}

	public Team getTeam(int teamId) {
		return teamRepository.findById(teamId)
				.orElseThrow(() -> new ResourceNotFoundException("Team " + teamId + " not found"));
	}

	@Transactional
	public Team createTeam(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Team name is required");
		}
		Team team = Team.builder()
				.teamName(name.trim())
				.build();
		return teamRepository.save(team);
	}

	@Transactional
	public TeamMember addMember(int teamId, String name, String email, String password) {
		Team team = getTeam(teamId);
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Member name is required");
		}
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Member email is required");
		}
		if (password == null) {
			password = "";
		}

		TeamMember member = new TeamMember();
		member.setName(name.trim());
		member.setEmail(email.trim());
		member.setPassword(password);
		member.setRole(Role.TEAM_MEMBER);
		member.setTeam(team);
		team.addMember(member);
		return teamMemberRepository.save(member);
	}

	public TeamMember getMember(int teamMemberId) {
		return teamMemberRepository.findById(teamMemberId)
				.orElseThrow(() -> new ResourceNotFoundException("TeamMember " + teamMemberId + " not found"));
	}
}
