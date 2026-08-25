package com.jobhunter.JobHunter.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jobhunter.JobHunter.enumeration.EngagementStatus;
import com.jobhunter.JobHunter.model.Engagement;
import com.jobhunter.JobHunter.model.Freelancer;
import com.jobhunter.JobHunter.repository.*;
import com.jobhunter.JobHunter.service.FreelancerService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class EngagementControllerTest {
    @Mock private FreelancerService freelancerService;
    @Mock private FreelancerRepository freelancerRepository;
    @Mock private EngagementRepository engagementRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private EmployerRepository employerRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private EngagementController engagementController;

    @Test
    void acceptEngagement_allowsOnlyAssignedFreelancerToAcceptPendingOffer() {
        Freelancer freelancer = freelancer(11L);
        Engagement engagement = new Engagement();
        engagement.setFreelancer(freelancer);
        engagement.setStatus(EngagementStatus.PENDING);
        when(freelancerRepository.findByUserEmail("freelancer@test")).thenReturn(Optional.of(freelancer));
        when(engagementRepository.findById(4L)).thenReturn(Optional.of(engagement));

        assertEquals("redirect:/my-engagements", engagementController.acceptEngagement(4L,
                new TestingAuthenticationToken("freelancer@test", null)));

        assertEquals(EngagementStatus.ACCEPTED, engagement.getStatus());
    }

    @Test
    void acceptEngagement_rejectsOfferBelongingToAnotherFreelancer() {
        Freelancer caller = freelancer(11L);
        Engagement engagement = new Engagement();
        engagement.setFreelancer(freelancer(12L));
        engagement.setStatus(EngagementStatus.PENDING);
        when(freelancerRepository.findByUserEmail("freelancer@test")).thenReturn(Optional.of(caller));
        when(engagementRepository.findById(4L)).thenReturn(Optional.of(engagement));

        assertThrows(RuntimeException.class, () -> engagementController.acceptEngagement(4L,
                new TestingAuthenticationToken("freelancer@test", null)));

        assertEquals(EngagementStatus.PENDING, engagement.getStatus());
    }

    @Test
    void acceptEngagement_rejectsAnOfferThatWasAlreadyDecided() {
        Freelancer freelancer = freelancer(11L);
        Engagement engagement = new Engagement();
        engagement.setFreelancer(freelancer);
        engagement.setStatus(EngagementStatus.REJECTED);
        when(freelancerRepository.findByUserEmail("freelancer@test")).thenReturn(Optional.of(freelancer));
        when(engagementRepository.findById(4L)).thenReturn(Optional.of(engagement));

        assertThrows(RuntimeException.class, () -> engagementController.acceptEngagement(4L,
                new TestingAuthenticationToken("freelancer@test", null)));

        assertEquals(EngagementStatus.REJECTED, engagement.getStatus());
    }

    @Test
    void rejectEngagement_allowsAssignedFreelancerToRejectPendingOffer() {
        Freelancer freelancer = freelancer(11L);
        Engagement engagement = new Engagement();
        engagement.setFreelancer(freelancer);
        engagement.setStatus(EngagementStatus.PENDING);
        when(freelancerRepository.findByUserEmail("freelancer@test")).thenReturn(Optional.of(freelancer));
        when(engagementRepository.findById(4L)).thenReturn(Optional.of(engagement));

        assertEquals("redirect:/my-engagements", engagementController.rejectEngagement(4L,
                new TestingAuthenticationToken("freelancer@test", null)));

        assertEquals(EngagementStatus.REJECTED, engagement.getStatus());
    }

    private Freelancer freelancer(Long id) {
        Freelancer freelancer = new Freelancer();
        freelancer.setId(id);
        return freelancer;
    }
}
