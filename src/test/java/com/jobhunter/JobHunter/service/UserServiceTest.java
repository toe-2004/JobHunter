package com.jobhunter.JobHunter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("sam");
        user.setEmail("sam@example.com");
        user.setPassword("password123");
    }

    @Test
    void registerUser_encodesPasswordAssignsGuestRoleAndSaves() {
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");

        String result = userService.registerUser(user, null);

        assertEquals("success", result);
        assertEquals(Role.GUEST, user.getRole());
        assertEquals("encoded-password", user.getPassword());
        verify(userRepository).save(same(user));
    }

    @Test
    void registerUser_rejectsDuplicateEmailWithoutSaving() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertEquals("Email already exists!", userService.registerUser(user, null));

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void registerUser_rejectsDuplicateUsernameWithoutSaving() {
        when(userRepository.existsByName(user.getName())).thenReturn(true);

        assertEquals("Username already exists!", userService.registerUser(user, null));

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }
}
