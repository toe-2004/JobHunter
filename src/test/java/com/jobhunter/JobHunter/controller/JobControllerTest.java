package com.jobhunter.JobHunter.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jobhunter.JobHunter.enumeration.JobStatus;
import com.jobhunter.JobHunter.model.*;
import com.jobhunter.JobHunter.repository.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {
    @Mock private UserRepository userRepository;
    @Mock private EmployerRepository employerRepository;
    @Mock private JobRepository jobRepository;
    @Mock private SkillRepository skillRepository;
    @Mock private JobSkillRepository jobSkillRepository;
    @InjectMocks private JobController jobController;

    @BeforeEach
    void authenticate() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("employer@test", null));
    }

    @Test
    void createJob_assignsCurrentEmployerOpenStatusAndSelectedSkills() {
        User user = new User(); user.setId(3L);
        Employer employer = new Employer(); employer.setId(4L);
        Job job = new Job();
        Skill skill = new Skill(); skill.setId(9L);
        when(userRepository.findByEmail("employer@test")).thenReturn(Optional.of(user));
        when(employerRepository.findByUserId(3L)).thenReturn(Optional.of(employer));
        when(jobRepository.save(job)).thenReturn(job);
        when(skillRepository.findById(9L)).thenReturn(Optional.of(skill));

        assertEquals("redirect:/my-jobs", jobController.createJob(job, List.of(9L)));

        assertSame(employer, job.getEmployer());
        assertEquals(JobStatus.OPEN, job.getStatus());
        verify(jobSkillRepository).save(argThat(link -> link.getJob() == job && link.getSkill() == skill));
    }

    @Test
    void changeJobStatus_togglesOpenJobToClosed() {
        Job job = new Job(); job.setStatus(JobStatus.OPEN);
        when(jobRepository.findById(5L)).thenReturn(Optional.of(job));

        assertEquals("redirect:/my-jobs", jobController.changeJobStatus(5L));

        assertEquals(JobStatus.CLOSED, job.getStatus());
        verify(jobRepository).save(job);
    }
}
