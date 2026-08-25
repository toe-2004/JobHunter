package com.jobhunter.JobHunter.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jobhunter.JobHunter.controller.HomeController;
import com.jobhunter.JobHunter.repository.CategoryRepository;
import com.jobhunter.JobHunter.repository.JobRepository;
import com.jobhunter.JobHunter.repository.EmployerRepository;
import com.jobhunter.JobHunter.repository.FreelancerRepository;
import com.jobhunter.JobHunter.repository.EngagementRepository;
import com.jobhunter.JobHunter.service.EmployerService;
import com.jobhunter.JobHunter.service.FreelancerService;
import com.jobhunter.JobHunter.service.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class SecurityRouteAccessTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private CustomLoginSuccessHandler customLoginSuccessHandler;
    @MockitoBean private FreelancerService freelancerService;
    @MockitoBean private CategoryRepository categoryRepository;
    @MockitoBean private JobRepository jobRepository;
    @MockitoBean private EmployerRepository employerRepository;
    @MockitoBean private FreelancerRepository freelancerRepository;
    @MockitoBean private EngagementRepository engagementRepository;
    @MockitoBean private EmployerService employerService;
    @MockitoBean private JobService jobService;

    @Test
    void publicHomeRouteIsAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/viewHomeJob"));
    }

    @Test
    void protectedRouteRedirectsAnonymousUsersToLogin() throws Exception {
        mockMvc.perform(get("/all-users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void protectedRouteIsReachableAfterAuthentication() throws Exception {
        mockMvc.perform(get("/all-users").with(user("member")))
                .andExpect(status().isNotFound());
    }
}
