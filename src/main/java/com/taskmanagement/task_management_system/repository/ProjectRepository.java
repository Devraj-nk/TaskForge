package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.Project;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	@EntityGraph(attributePaths = {"team", "tasks", "sprints"})
	Optional<Project> findWithDetailsByProjectId(int projectId);
}
