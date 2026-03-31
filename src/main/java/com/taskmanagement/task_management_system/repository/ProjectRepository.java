package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.Project;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	// Avoid Hibernate MultipleBagFetchException by not fetching multiple List (bag) collections at once.
	@EntityGraph(attributePaths = {"team"})
	Optional<Project> findWithDetailsByProjectId(int projectId);
}
