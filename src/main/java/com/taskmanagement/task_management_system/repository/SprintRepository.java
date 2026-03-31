package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.Sprint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {

	List<Sprint> findByProject_ProjectIdOrderBySprintIdDesc(int projectId);
}
