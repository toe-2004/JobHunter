package com.jobhunter.JobHunter.repository;

import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    long countByEmployer(Employer employer);

    List<Job> findTop5ByEmployerOrderByCreatedAtDesc(Employer employer);
}
