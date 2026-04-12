package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.Team;
import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.model.Role;
import com.taskmanagement.task_management_system.model.Project;
import com.taskmanagement.task_management_system.repository.ProjectRepository;
import com.taskmanagement.task_management_system.repository.TeamRepository;
import com.taskmanagement.task_management_system.repository.TeamMemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final ProjectRepository projectRepository;

	public TeamService(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, ProjectRepository projectRepository) {
		this.teamRepository = teamRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.projectRepository = projectRepository;
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

	@Transactional
	public TeamMember assignMemberToProject(int teamMemberId, int projectId) {
		TeamMember member = getMember(teamMemberId);
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project " + projectId + " not found"));

		Team targetTeam = project.getTeam();
		if (targetTeam == null) {
			// Since Project and Team are 1:1 in the current UI, create the Team automatically if missing.
			targetTeam = Team.builder()
					.teamName(project.getName())
					.project(project)
					.build();
			targetTeam = teamRepository.save(targetTeam);
			project.setTeam(targetTeam);
		}

		Team currentTeam = member.getTeam();
		if (currentTeam != null && currentTeam.getTeamId() == targetTeam.getTeamId()) {
			return member;
		}
		if (currentTeam != null) {
			currentTeam.removeMember(member);
		}
		targetTeam.addMember(member);
		return teamMemberRepository.save(member);
	}

	@Transactional
	public TeamMember removeMemberFromTeam(int teamMemberId) {
		TeamMember member = getMember(teamMemberId);
		Team currentTeam = member.getTeam();
		if (currentTeam != null) {
			currentTeam.removeMember(member);
		}
		member.setTeam(null);
		return teamMemberRepository.save(member);
	}
}
