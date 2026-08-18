package com.jobhunter.JobHunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.*;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

	List<Application> findTop5ByJobEmployerOrderByCreatedAtDesc(Employer employer);
    List<Application> findByFreelancer(Freelancer freelancer);
    List<Application> findByJob(Job job);

    boolean existsByJobAndFreelancer(Job job,Freelancer freelancer);
    boolean existsByFreelancerIdAndJobId(Long freelancerId, Long jobId);
    boolean existsByFreelancerAndJob(Freelancer freelancer, Job job);
    
    long countByFreelancerId(Long freelancerId);

    List<Application> findTop4ByFreelancerIdOrderByCreatedAtDesc(Long freelancerId);
    
    List<Application> findByFreelancerIdOrderByCreatedAtDesc(Long freelancerId);
    
}
