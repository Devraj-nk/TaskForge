package com.taskmanagement.task_management_system.config;

import com.taskmanagement.task_management_system.service.planning.SprintPlanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanningConfig {

    @Bean
    public SprintPlanner sprintPlanner() {
        return SprintPlanner.builder()
                .dependencyAware(true)
                .build();
    }
}
