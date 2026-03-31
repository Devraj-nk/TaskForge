package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskRepository extends JpaRepository<SubTask, Integer> {
}
