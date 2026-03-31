-- MySQL schema for TaskForge (matches JPA mappings in model/)

CREATE TABLE IF NOT EXISTS project (
  project_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  PRIMARY KEY (project_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
  user_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','PROJECT_MANAGER','TEAM_MEMBER') NOT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS team (
  team_id INT NOT NULL AUTO_INCREMENT,
  team_name VARCHAR(255) NOT NULL,
  project_id INT NULL,
  PRIMARY KEY (team_id),
  UNIQUE KEY uk_team_project (project_id),
  CONSTRAINT fk_team_project FOREIGN KEY (project_id) REFERENCES project(project_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS team_member (
  user_id INT NOT NULL,
  team_id INT NULL,
  PRIMARY KEY (user_id),
  KEY ix_team_member_team_id (team_id),
  CONSTRAINT fk_team_member_user FOREIGN KEY (user_id) REFERENCES users(user_id),
  CONSTRAINT fk_team_member_team FOREIGN KEY (team_id) REFERENCES team(team_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sprint (
  sprint_id INT NOT NULL AUTO_INCREMENT,
  start_date DATETIME NULL,
  end_date DATETIME NULL,
  project_id INT NOT NULL,
  PRIMARY KEY (sprint_id),
  KEY ix_sprint_project_id (project_id),
  CONSTRAINT fk_sprint_project FOREIGN KEY (project_id) REFERENCES project(project_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS task (
  task_id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  priority INT NOT NULL,
  status ENUM('CREATED','ASSIGNED','IN_PROGRESS','BLOCKED','COMPLETED') NOT NULL,
  project_id INT NOT NULL,
  assigned_to INT NULL,
  PRIMARY KEY (task_id),
  KEY ix_task_project_id (project_id),
  KEY ix_task_assigned_to (assigned_to),
  CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(project_id),
  CONSTRAINT fk_task_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sub_task (
  subtask_id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  status ENUM('CREATED','ASSIGNED','IN_PROGRESS','BLOCKED','COMPLETED') NOT NULL,
  task_id INT NOT NULL,
  PRIMARY KEY (subtask_id),
  KEY ix_subtask_task_id (task_id),
  CONSTRAINT fk_subtask_task FOREIGN KEY (task_id) REFERENCES task(task_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS dependency (
  dependency_id INT NOT NULL AUTO_INCREMENT,
  dependency_type VARCHAR(255) NULL,
  task_id INT NOT NULL,
  prerequisite_task_id INT NULL,
  PRIMARY KEY (dependency_id),
  KEY ix_dependency_task_id (task_id),
  KEY ix_dependency_prereq_task_id (prerequisite_task_id),
  CONSTRAINT fk_dependency_task FOREIGN KEY (task_id) REFERENCES task(task_id),
  CONSTRAINT fk_dependency_prereq FOREIGN KEY (prerequisite_task_id) REFERENCES task(task_id)
) ENGINE=InnoDB;
