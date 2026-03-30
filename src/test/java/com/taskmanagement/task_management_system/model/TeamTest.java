package com.taskmanagement.task_management_system.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TeamTest {

    @Test
    void addAndRemoveMemberShouldUpdateMembership() {
        Team team = new Team(1, "Platform");
        TeamMember member = new TeamMember(5, "Casey", "casey@mail.com", "pw");

        team.addMember(member);
        assertEquals(1, team.getTeamSize());
        assertTrue(team.hasMember(member));

        team.addMember(member);
        assertEquals(1, team.getTeamSize());

        team.removeMember(member);
        assertEquals(0, team.getTeamSize());
    }

    @Test
    void membersListShouldBeUnmodifiable() {
        Team team = new Team(1, "Platform");
        assertThrows(UnsupportedOperationException.class,
                () -> team.getMembers().add(new TeamMember()));
    }
}