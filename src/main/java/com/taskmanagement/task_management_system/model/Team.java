package com.taskmanagement.task_management_system.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "team")
/**
 * **Design patterns used:**
 * - Domain Model (JPA Entity): this class is a persistence-backed domain entity.
 * - Builder: `Team.builder()...build()` provides controlled object creation.
 *
 * **GRASP:**
 * - Information Expert: owns and manages `members` and keeps the TeamMember.team association consistent.
 *
 * **SOLID:**
 * - SRP: represents Team state + membership invariants (relationship consistency).
 * - Encapsulation (supports maintainability): exposes an unmodifiable view of members.
 */
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private int teamId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", unique = true)
    private Project project;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> members = new ArrayList<>();

    public Team() {}

    public Team(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    /**
     * Builder pattern entry-point.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Integer teamId;
        private String teamName;
        private Project project;

        private Builder() {
        }

        public Builder teamId(int teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder project(Project project) {
            this.project = project;
            return this;
        }

        public Team build() {
            Team team = new Team();
            if (teamId != null) {
                team.setTeamId(teamId);
            }
            team.setTeamName(teamName);
            team.setProject(project);
            return team;
        }
    }

    // --- Getters & Setters ---

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public List<TeamMember> getMembers() {
        // GRASP (Low Coupling) + SOLID (Encapsulation): callers can't mutate the internal list directly.
        return Collections.unmodifiableList(members);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    // --- UML Methods ---

    /**
     * Adds a TeamMember to this team.
     */
    public void addMember(TeamMember member) {
        // GRASP (Information Expert): Team maintains membership and the bidirectional association.
        Objects.requireNonNull(member, "member cannot be null");
        if (!members.contains(member)) {
            members.add(member);
			member.setTeam(this);
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
        // GRASP (Information Expert): Team removes membership and clears the back-reference.
        Objects.requireNonNull(member, "member cannot be null");
        if (members.remove(member)) {
			member.setTeam(null);
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