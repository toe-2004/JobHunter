package com.jobhunter.JobHunter.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Engagement;
import com.jobhunter.JobHunter.model.Freelancer;

@Repository
public interface EngagementRepository extends JpaRepository<Engagement, Long> {

	List<Engagement> findByFreelancer(Freelancer freelancer);
	
	List<Engagement> findByEmployer(Employer employer);
	
	long countByFreelancerId(Long freelancerId);

    List<Engagement> findTop4ByFreelancerIdOrderByStartDateDesc(Long freelancerId);
    
    List<Engagement> findByFreelancerIdOrderByStartDateDesc(Long freelancerId);

    List<Engagement> findByFreelancerOrderByCreatedAtDesc(Freelancer freelancer);
    
    List<Engagement> findByEmployerOrderByCreatedAtDesc(Employer employer);
	
}
