package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {

	@EntityGraph(attributePaths = {"members"})
	Optional<Team> findWithDetailsByTeamId(int teamId);
}