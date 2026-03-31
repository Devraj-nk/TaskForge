package com.taskmanagement.task_management_system.config;

import com.taskmanagement.task_management_system.controller.ui.auth.SessionConstants;
import com.taskmanagement.task_management_system.controller.ui.auth.SessionUser;
import com.taskmanagement.task_management_system.model.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String path = request.getRequestURI();

        if (path.equals("/") || path.startsWith("/login")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        SessionUser sessionUser = null;
        if (session != null) {
            Object value = session.getAttribute(SessionConstants.SESSION_USER);
            if (value instanceof SessionUser su) {
                sessionUser = su;
            }
        }

        if (sessionUser == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (path.startsWith("/member")) {
            if (sessionUser.role() != Role.TEAM_MEMBER) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        }

        if (path.startsWith("/projects") || path.startsWith("/teams") || path.startsWith("/tasks")) {
            if (sessionUser.role() != Role.PROJECT_MANAGER) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        }

        return true;
    }
}
