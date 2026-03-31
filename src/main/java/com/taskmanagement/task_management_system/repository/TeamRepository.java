package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}