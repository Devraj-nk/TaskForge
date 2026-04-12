package com.taskmanagement.task_management_system.controller.ui.auth;

import com.taskmanagement.task_management_system.model.Role;
import com.taskmanagement.task_management_system.model.TeamMember;
import com.taskmanagement.task_management_system.model.User;
import com.taskmanagement.task_management_system.service.AuthService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
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

    @GetMapping("/login/manager")
    public String managerLoginPage() {
        return "login-manager";
    }

    @GetMapping("/login/member")
    public String memberLoginPage() {
        return "login-member";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/register/manager")
    public String registerManagerPage() {
        return "register-manager";
    }

    @GetMapping("/register/member")
    public String registerMemberPage() {
        return "register-member";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        // Backwards-compatible registration endpoint (defaults to Team Member).
        return registerMember(name, email, password, session, redirectAttributes);
    }

    @PostMapping("/register/member")
    public String registerMember(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            TeamMember member = authService.registerTeamMember(name, email, password);
            session.setAttribute(
                    SessionConstants.SESSION_USER,
                    new SessionUser(member.getUserId(), member.getName(), member.getRole())
            );
            redirectAttributes.addFlashAttribute("message", "Account created");
            return "redirect:/member";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/register/member";
        }
    }

    @PostMapping("/register/manager")
    public String registerManager(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User manager = authService.registerProjectManager(name, email, password);
            session.setAttribute(
                    SessionConstants.SESSION_USER,
                    new SessionUser(manager.getUserId(), manager.getName(), manager.getRole())
            );
            redirectAttributes.addFlashAttribute("message", "Manager account created");
            return "redirect:/projects";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/register/manager";
        }
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

    @PostMapping("/login/manager")
    public String loginManager(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Optional<User> user = authService.authenticateWithRole(Role.PROJECT_MANAGER, email, password);
        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Invalid manager credentials");
            return "redirect:/login/manager";
        }

        User u = user.get();
        session.setAttribute(
                SessionConstants.SESSION_USER,
                new SessionUser(u.getUserId(), u.getName(), u.getRole())
        );
        return "redirect:/projects";
    }

    @PostMapping("/login/member")
    public String loginMember(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Optional<User> user = authService.authenticateWithRole(Role.TEAM_MEMBER, email, password);
        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Invalid member credentials");
            return "redirect:/login/member";
        }

        User u = user.get();
        session.setAttribute(
                SessionConstants.SESSION_USER,
                new SessionUser(u.getUserId(), u.getName(), u.getRole())
        );
        return "redirect:/member";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
