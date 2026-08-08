package com.jobhunter.JobHunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.User;

import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    boolean existsByCompanyEmail(String companyEmail);

    Optional<Employer> findByUser(User user);

}
