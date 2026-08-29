package com.jobhunter.JobHunter.repository;

import com.jobhunter.JobHunter.enumeration.JobStatus;
import com.jobhunter.JobHunter.model.Category;
import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    long countByEmployer(Employer employer);

    List<Job> findTop5ByEmployerOrderByCreatedAtDesc(Employer employer);
    
    List<Job> findByEmployer(Employer employer);
    
    List<Job> findTop3ByOrderByCreatedAtDesc();
    List<Job> findTop3ByStatusOrderByCreatedAtDesc(JobStatus status);
    List<Job> findTop6ByOrderByCreatedAtDesc();
    List<Job> findByCategoryId(Long categoryId);
    
    List<Job> findByCategory(Category category);
    
    List<Job> findByTitleContainingIgnoreCase(String keyword);

    List<Job> findByEmployerCompanyNameContainingIgnoreCase(String keyword);

    List<Job> findByCategoryNameContainingIgnoreCase(String keyword);
    long countByStatus(JobStatus status);
    List<Job> findAllByOrderByCreatedAtDesc();
    
    List<Job> findByEmployerOrderByCreatedAtDesc(Employer employer);
    List<Job> findByStatusOrderByCreatedAtDesc(JobStatus status);
    long countByTitleAndStatus(String title, JobStatus status);
    long countDistinctEmployerIdByTitleAndStatus(String title,JobStatus status);
   
}
