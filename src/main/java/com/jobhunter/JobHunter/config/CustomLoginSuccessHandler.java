package com.jobhunter.JobHunter.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        if (user.getRole() == Role.GUEST) {

            response.sendRedirect("/choose-role");

        } else if (user.getRole() == Role.EMPLOYER) {

            response.sendRedirect("/employer/dashboard");

        } else if (user.getRole() == Role.FREELANCER) {

            response.sendRedirect("/freelancer/dashboard");

        } else {

            response.sendRedirect("/");

        }

    }
}