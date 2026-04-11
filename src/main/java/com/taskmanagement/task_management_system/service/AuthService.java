package com.taskmanagement.task_management_system.service;

import com.taskmanagement.task_management_system.model.User;
import com.taskmanagement.task_management_system.repository.UserRepository;
import java.util.Optional;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
