# TaskForge

TaskForge is a Spring Boot task management system with both:
- a server-rendered UI (Thymeleaf), and
- a JSON REST API.

It supports projects, teams, sprints, tasks, subtasks, and task dependencies (blockers).

> Note: the original package name contained an invalid `-` character; the codebase uses `com.taskmanagement.task_management_system`.

## Tech stack
- Java 21
- Spring Boot 4.0.4
- Spring Web MVC + Thymeleaf
- Spring Data JPA
- MySQL
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Features (high-level)
- Projects with tasks and sprints
- Teams and team members
- Assign tasks to team members
- Dependencies between tasks (a task can be blocked until prerequisites are completed)
- Member dashboard to view and update assigned tasks
- REST API for projects, tasks, teams, and sprints

## Quick start

### Prerequisites
- JDK 21 installed
- MySQL running locally

### Configure database
The app uses SQL scripts for schema creation and JPA for mapping validation:
- `spring.sql.init.mode=always`
- `spring.jpa.hibernate.ddl-auto=validate`

Credentials are NOT hardcoded. You can configure them via a local-only secrets file or environment variables.

#### Option A: local secrets file (recommended)
1. Copy `taskforge-secrets.properties.example` → `taskforge-secrets.properties`
2. Fill in your local MySQL username/password

`taskforge-secrets.properties` is ignored by git via `.gitignore`.

#### Option B: environment variables
Set the following env vars:
- `TASKFORGE_DB_USER=...`
- `TASKFORGE_DB_PASS=...`

You can also override the JDBC URL if needed:
- `SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/taskforge?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`

Schema is created from `src/main/resources/schema.sql`.

### Run (Windows)
```powershell
.\mvnw.cmd spring-boot:run
```

### Run (macOS/Linux)
```bash
./mvnw spring-boot:run
```

Then open:
- `http://localhost:8080/`
- Login page: `http://localhost:8080/login`

### Seeded login
`src/main/resources/data.sql` seeds a default user:
- Email: `pm@taskforge.com`
- Password: `pm123`

## UI routes
- `/` → Home
- `/login` → Login
- `/projects` → Project list + create
- `/projects/{projectId}` → Project details (tasks, sprints, team assignment)
- `/tasks` and `/tasks/{taskId}` → Task list/details and status updates
- `/teams` and `/teams/{teamId}` → Teams and members
- `/member` → Team member dashboard
- `/member/sprints/{sprintId}` → Member sprint view

## REST API routes
Base paths are JSON (`@RestController`).

### Projects
- `GET /api/projects` → list projects
- `GET /api/projects/{projectId}` → project details (team, sprints, tasks)
- `POST /api/projects` → create project
- `PUT /api/projects/{projectId}` → update project
- `DELETE /api/projects/{projectId}` → delete project
- `GET /api/projects/{projectId}/tasks` → list tasks for project
- `POST /api/projects/{projectId}/tasks` → create a task under a project
- `PUT /api/projects/{projectId}/team/{teamId}` → assign an existing team to a project

### Sprints
- `GET /api/projects/{projectId}/sprints` → list sprints for project
- `POST /api/projects/{projectId}/sprints` → create sprint

### Tasks
- `GET /api/tasks` → list tasks
- `GET /api/tasks/{taskId}` → task details
- `PUT /api/tasks/{taskId}` → update task (title/description/priority/status)
- `DELETE /api/tasks/{taskId}` → delete task
- `POST /api/tasks/{taskId}/assign` → assign to a team member

### Teams
- `GET /api/teams` → list teams
- `GET /api/teams/{teamId}` → team details (members)
- `POST /api/teams` → create team
- `POST /api/teams/{teamId}/members` → add team member

### Error handling
The API returns consistent JSON errors for common cases:
- Not found → 404 (`ResourceNotFoundException`)
- Invalid inputs → 400 (`IllegalArgumentException`)

## Database model
Core tables (see `sql_schema.txt` and `src/main/resources/schema.sql`):
- `project`
- `users` (base user table with role)
- `team`
- `team_member` (specialization)
- `sprint`
- `task`
- `sub_task`
- `dependency`

Key relationships:
- Project → Tasks (1:N)
- Project → Sprints (1:N)
- Project → Team (1:1)
- Task → SubTasks (1:N)
- Task → Dependencies (1:N)
- Dependencies link tasks (prerequisite ↔ dependent)

## Project structure
- `src/main/java/.../model` → JPA entities (domain model)
- `src/main/java/.../repository` → Spring Data repositories
- `src/main/java/.../service` → business logic (service layer)
- `src/main/java/.../controller` → REST controllers (`/api/...`)
- `src/main/java/.../controller/ui` → Thymeleaf UI controllers
- `src/main/resources/templates` → Thymeleaf templates
- `src/main/resources/schema.sql`, `data.sql` → DB init scripts

## Design notes (SOLID/GRASP/patterns)
- **Layering**: controllers delegate to services, services use repositories.
- **DTOs**: request/response shapes are defined as records in `controller/dto/*Dtos.java`.
- **SRP improvement**: `User` is a pure entity (authentication logic lives in `AuthService`).
- **Builder pattern**:
  - `SubTask.builder()...build()` creates valid `SubTask` instances with required fields.
  - Other model/service code also uses builders in a similar style.

## Build & test
Build:
```bash
./mvnw -DskipTests package
```

Tests:
```bash
./mvnw test
```

Note: tests load the Spring context and require a working MySQL connection matching your datasource settings.

## Diagrams
See the `diagrams/` folder for UML/use-case/activity diagrams.
