package com.jobhunter.JobHunter.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jobhunter.JobHunter.dto.EmployerRegistrationDto;
import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock private FreelancerRepository freelancerRepository;
    @InjectMocks private FreelancerService freelancerService;
    @Mock private UserService userService;
    @Mock private EmployerRepository employerRepository;
    @Mock private JobRepository jobRepository;
    @Mock private UserRepository userRepository;
    @Mock private ApplicationRepository applicationRepository;
    private EmployerService employerService;

    @BeforeEach
    void setUpEmployerService() {
        employerService = new EmployerService(userService);
        ReflectionTestUtils.setField(employerService, "employerRepository", employerRepository);
        ReflectionTestUtils.setField(employerService, "userRepository", userRepository);
        ReflectionTestUtils.setField(employerService, "jobRepository", jobRepository);
        ReflectionTestUtils.setField(employerService, "applicationRepository", applicationRepository);
    }

    @Test
    void getFreelancerById_throwsWhenProfileDoesNotExist() {
        when(freelancerRepository.findById(42L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> freelancerService.getFreelancerById(42L));

        assertTrue(exception.getMessage().contains("42"));
    }

    @Test
    void createEmployer_assignsEmployerRoleAndPersistsProfile() {
        User user = new User();
        user.setId(7L);
        EmployerRegistrationDto form = form();
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        when(employerRepository.save(any(Employer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employer employer = employerService.createEmployer(7L, form);

        assertEquals(Role.EMPLOYER, user.getRole());
        assertSame(user, employer.getUser());
        assertEquals("Acme", employer.getCompanyName());
        verify(userRepository).save(user);
        verify(employerRepository).save(employer);
    }

    @Test
    void createEmployer_rejectsDuplicateCompanyEmail() {
        User user = new User();
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        when(employerRepository.existsByCompanyEmail("contact@acme.test")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> employerService.createEmployer(7L, form()));

        verify(employerRepository, never()).save(any());
    }

    @Test
    void createEmployer_rejectsUserWhoAlreadyHasAnEmployerProfile() {
        User user = new User();
        user.setEmployer(new Employer());
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> employerService.createEmployer(7L, form()));

        verify(employerRepository, never()).save(any());
    }

    private EmployerRegistrationDto form() {
        EmployerRegistrationDto form = new EmployerRegistrationDto();
        form.setCompanyName("Acme");
        form.setCompanyEmail("contact@acme.test");
        form.setCompanyPhone("123");
        form.setCompanyDescription("Description");
        form.setCompanyLocation("Yangon");
        return form;
    }
}
