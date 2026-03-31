package com.taskmanagement.task_management_system.controller.ui.auth;

import com.taskmanagement.task_management_system.model.Role;
import com.taskmanagement.task_management_system.model.User;
import com.taskmanagement.task_management_system.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthPageController {

    private final AuthService authService;

    public AuthPageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        return authService.authenticate(email, password)
                .map(user -> {
                    session.setAttribute(
                            SessionConstants.SESSION_USER,
                            new SessionUser(user.getUserId(), user.getName(), user.getRole())
                    );
                    if (user.getRole() == Role.TEAM_MEMBER) {
                        return "redirect:/member";
                    }
                    if (user.getRole() == Role.PROJECT_MANAGER) {
                        return "redirect:/projects";
                    }
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("message", "Invalid email/password");
                    return "redirect:/login";
                });
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
