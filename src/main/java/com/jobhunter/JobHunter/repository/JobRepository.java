package com.jobhunter.JobHunter.repository;

import com.jobhunter.JobHunter.model.Category;
import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    long countByEmployer(Employer employer);

    List<Job> findTop5ByEmployerOrderByCreatedAtDesc(Employer employer);
    
    List<Job> findByEmployer(Employer employer);
    
    List<Job> findTop3ByOrderByCreatedAtDesc();
    List<Job> findTop6ByOrderByCreatedAtDesc();
    List<Job> findByCategoryId(Long categoryId);
    
    List<Job> findByCategory(Category category);

    
}
