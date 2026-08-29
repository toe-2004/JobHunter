// package com.jobhunter.JobHunter.controller;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertSame;
// import static org.mockito.Mockito.*;

// import com.jobhunter.JobHunter.enumeration.Role;
// import com.jobhunter.JobHunter.model.Freelancer;
// import com.jobhunter.JobHunter.model.User;
// import com.jobhunter.JobHunter.repository.*;
// import com.jobhunter.JobHunter.service.FreelancerService;
// import java.util.Optional;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.authentication.TestingAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;

// @ExtendWith(MockitoExtension.class)
// class FreelancerControllerTest {
//     @Mock private UserRepository userRepository;
//     @Mock private FreelancerRepository freelancerRepository;
//     @Mock private ApplicationRepository applicationRepository;
//     @Mock private EngagementRepository engagementRepository;
//     @Mock private SkillRepository skillRepository;
//     @Mock private FreelancerService freelancerService;
//     @Mock private FreelancerSkillRepository freelancerSkillRepository;
//     @InjectMocks private FreelancerController freelancerController;

//     @AfterEach
//     void clearSecurityContext() {
//         SecurityContextHolder.clearContext();
//     }

//     @Test
//     void saveProfile_createsProfileLinkedToCurrentUserAndAssignsFreelancerRole() throws Exception {
//         User user = new User();
//         user.setRole(Role.GUEST);
//         Freelancer profile = new Freelancer();
//         profile.setTitle("Designer");
//         SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("user@test", null));
//         when(userRepository.findByEmail("user@test")).thenReturn(Optional.of(user));
//         when(freelancerRepository.findByUser(user)).thenReturn(Optional.empty());
//         when(freelancerRepository.save(profile)).thenReturn(profile);

//         assertEquals("redirect:/freelancer/profile", freelancerController.createFreelancer(profile, null, null));

//         assertSame(user, profile.getUser());
//         assertEquals(Role.FREELANCER, user.getRole());
//         verify(freelancerRepository).save(profile);
//         verify(userRepository).save(user);
//         verify(freelancerSkillRepository).deleteByFreelancer(profile);
//     }
// }
