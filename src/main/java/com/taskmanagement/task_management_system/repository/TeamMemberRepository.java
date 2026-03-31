package com.taskmanagement.task_management_system.repository;

import com.taskmanagement.task_management_system.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {
}
