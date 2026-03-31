package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findByAssignedTo_UserId(int userId);
}
