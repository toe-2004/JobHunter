package com.jobhunter.JobHunter.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jobhunter.JobHunter.enumeration.ApplicationStatus;
import com.jobhunter.JobHunter.model.Application;
import com.jobhunter.JobHunter.model.Freelancer;
import com.jobhunter.JobHunter.model.Job;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {
    @Mock private ApplicationRepository applicationRepository;
    @Mock private JobRepository jobRepository;
    @Mock private UserRepository userRepository;
    @Mock private FreelancerRepository freelancerRepository;
    @InjectMocks private ApplicationController applicationController;

    @Test
    void updateApplicationStatus_allowsPendingToShortlisted() {
        Application application = new Application();
        application.setStatus(ApplicationStatus.PENDING);
        Job job = new Job(); job.setId(8L); application.setJob(job);
        when(applicationRepository.findById(2L)).thenReturn(Optional.of(application));

        assertEquals("redirect:/employer/jobs/8/applications",
                applicationController.updateApplicationStatus(2L, ApplicationStatus.SHORTLISTED));

        assertEquals(ApplicationStatus.SHORTLISTED, application.getStatus());
        verify(applicationRepository).save(application);
    }

    @Test
    void updateApplicationStatus_rejectsInvalidPendingTransition() {
        Application application = new Application();
        application.setStatus(ApplicationStatus.PENDING);
        when(applicationRepository.findById(2L)).thenReturn(Optional.of(application));

        assertThrows(RuntimeException.class,
                () -> applicationController.updateApplicationStatus(2L, ApplicationStatus.ACCEPTED));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void showApplicationForm_rejectsNonFreelancerUsers() {
        Job job = new Job();
        job.setStatus(com.jobhunter.JobHunter.enumeration.JobStatus.OPEN);
        User employerUser = new User();
        employerUser.setRole(com.jobhunter.JobHunter.enumeration.Role.EMPLOYER);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("employer@test", null));
        when(jobRepository.findById(8L)).thenReturn(Optional.of(job));
        when(userRepository.findByEmail("employer@test")).thenReturn(Optional.of(employerUser));

        assertThrows(RuntimeException.class, () -> applicationController.showApplicationForm(8L, mock(org.springframework.ui.Model.class)));

        verifyNoInteractions(freelancerRepository);
    }

    @Test
    void applyJob_rejectsDuplicateApplicationBeforeSaving() {
        User user = new User();
        user.setRole(com.jobhunter.JobHunter.enumeration.Role.FREELANCER);
        Freelancer freelancer = new Freelancer();
        Job job = new Job();
        job.setStatus(com.jobhunter.JobHunter.enumeration.JobStatus.OPEN);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("freelancer@test", null));
        when(userRepository.findByEmail("freelancer@test")).thenReturn(Optional.of(user));
        when(freelancerRepository.findByUserEmail("freelancer@test")).thenReturn(Optional.of(freelancer));
        when(jobRepository.findById(8L)).thenReturn(Optional.of(job));
        when(applicationRepository.existsByJobAndFreelancer(job, freelancer)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> applicationController.applyJob(8L,
                new com.jobhunter.JobHunter.dto.ApplicationDto()));

        verify(applicationRepository, never()).save(any());
    }
}
