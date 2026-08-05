package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jobhunter.JobHunter.model.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Long>{

}
