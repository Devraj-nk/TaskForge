package com.taskmanagement.task_management_system.controller.dto;

import java.util.List;

public final class TeamDtos {
    private TeamDtos() {
    }

    public record CreateTeamRequest(String name) {
    }

    public record AddMemberRequest(String name, String email, String password) {
    }

    public record TeamMemberSummary(int userId, String name, String email) {
    }

    public record TeamSummary(int teamId, String teamName) {
    }

    public record TeamDetails(int teamId, String teamName, List<TeamMemberSummary> members) {
    }
}
