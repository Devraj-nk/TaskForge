package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.model.User;
import com.taskmanagement.task_management_system.model.Role;
import com.taskmanagement.task_management_system.repository.TeamMemberRepository;
import com.taskmanagement.task_management_system.repository.UserRepository;
import java.util.Optional;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    public AuthService(UserRepository userRepository, TeamMemberRepository teamMemberRepository) {
        this.userRepository = userRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    public Optional<User> authenticate(String email, String password) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        if (password == null) {
            password = "";
        }

        String normalizedEmail = email.trim();
        String normalizedPassword = password;

        return userRepository.findByEmail(normalizedEmail)
            .filter(user -> Objects.equals(user.getEmail(), normalizedEmail)
                && Objects.equals(user.getPassword(), normalizedPassword));
    }

    public Optional<User> authenticateWithRole(Role expectedRole, String email, String password) {
        return authenticate(email, password)
                .filter(u -> u.getRole() == expectedRole);
    }

    public TeamMember registerTeamMember(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        String normalizedEmail = email.trim();
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("An account with that email already exists");
        }

        TeamMember member = new TeamMember(0, name.trim(), normalizedEmail, password);
        return teamMemberRepository.save(member);
    }

    public User registerProjectManager(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        String normalizedEmail = email.trim();
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("An account with that email already exists");
        }

        User manager = User.createProjectManager(name.trim(), normalizedEmail, password);
        return userRepository.save(manager);
    }
}
