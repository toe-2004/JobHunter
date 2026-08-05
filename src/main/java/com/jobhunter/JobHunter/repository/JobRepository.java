package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jobhunter.JobHunter.model.Job;

public interface JobRepository extends JpaRepository<Job, Long>{

}
