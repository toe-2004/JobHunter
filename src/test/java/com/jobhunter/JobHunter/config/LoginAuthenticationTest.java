package com.jobhunter.JobHunter.config;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jobhunter.JobHunter.controller.UserController;
import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.UserRepository;
import com.jobhunter.JobHunter.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, CustomUserDetailsService.class, CustomLoginSuccessHandler.class})
class LoginAuthenticationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private PasswordEncoder passwordEncoder;
    @MockitoBean private UserRepository userRepository;
    @MockitoBean private UserService userService;

    @Test
    void loginWithValidCredentialsRedirectsFreelancerToDashboard() throws Exception {
        User user = user("freelancer@test", "password123", Role.FREELANCER);
        when(userRepository.findByEmail("freelancer@test")).thenReturn(Optional.of(user));

        mockMvc.perform(formLogin().user("freelancer@test").password("password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/freelancer/dashboard"));
    }

    @Test
    void loginWithInvalidCredentialsRedirectsToConfiguredFailureUrl() throws Exception {
        User user = user("freelancer@test", "password123", Role.FREELANCER);
        when(userRepository.findByEmail("freelancer@test")).thenReturn(Optional.of(user));

        mockMvc.perform(formLogin().user("freelancer@test").password("wrong-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    private User user(String email, String rawPassword, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return user;
    }
}
