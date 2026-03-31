package com.taskmanagement.task_management_system.controller.ui.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SessionUserAdvice {

    @ModelAttribute("sessionUser")
    public SessionUser sessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(SessionConstants.SESSION_USER);
        if (value instanceof SessionUser sessionUser) {
            return sessionUser;
        }
        return null;
    }
}
