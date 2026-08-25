package com.jobhunter.JobHunter.controller;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.jobhunter.JobHunter.config.CustomLoginSuccessHandler;
import com.jobhunter.JobHunter.config.SecurityConfig;
import com.jobhunter.JobHunter.repository.UserRepository;
import com.jobhunter.JobHunter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserRegistrationControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private UserService userService;
    @MockitoBean private UserRepository userRepository;
    @MockitoBean private CustomLoginSuccessHandler customLoginSuccessHandler;

    @Test
    void registerUser_returnsFormWhenRequiredFieldsAreInvalid() throws Exception {
        mockMvc.perform(multipart("/register")
                        .file(new MockMultipartFile("photo", new byte[0]))
                        .param("name", "")
                        .param("email", "not-an-email")
                        .param("password", "short")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "name", "email", "password"));

        verifyNoInteractions(userService);
    }
}
