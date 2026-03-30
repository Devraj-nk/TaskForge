package com.taskmanagement.task_management_system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Team {

    private int teamId;
    private String teamName;

    private List<TeamMember> members = new ArrayList<>();

    public Team() {}

    public Team(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    // --- Getters & Setters ---

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public List<TeamMember> getMembers() {
        return Collections.unmodifiableList(members);
    }

    // --- UML Methods ---

    /**
     * Adds a TeamMember to this team.
     */
    public void addMember(TeamMember member) {
        Objects.requireNonNull(member, "member cannot be null");
        if (!members.contains(member)) {
            members.add(member);
            System.out.println(member.getName() 
                + " added to team '" + teamName + "'");
        } else {
            System.out.println(member.getName() 
                + " is already in team '" + teamName + "'");
        }
    }

    /**
     * Removes a TeamMember from this team.
     */
    public void removeMember(TeamMember member) {
        Objects.requireNonNull(member, "member cannot be null");
        if (members.remove(member)) {
            System.out.println(member.getName() 
                + " removed from team '" + teamName + "'");
        } else {
            System.out.println(member.getName() 
                + " not found in team '" + teamName + "'");
        }
    }

    /**
     * Checks if a specific member belongs to this team.
     */
    public boolean hasMember(TeamMember member) {
        return members.contains(member);
    }

    /**
     * Returns number of members in this team.
     */
    public int getTeamSize() {
        return members.size();
    }

    @Override
    public String toString() {
        return "Team{id=" + teamId 
            + ", name='" + teamName 
            + "', members=" + members.size() + "}";
    }
}