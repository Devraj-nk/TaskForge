package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DependencyRepository extends JpaRepository<Dependency, Integer> {
}
