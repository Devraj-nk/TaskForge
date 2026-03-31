package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
