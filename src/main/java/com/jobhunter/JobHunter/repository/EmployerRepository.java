package com.jobhunter.JobHunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.User;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    boolean existsByCompanyEmail(String companyEmail);

    Optional<Employer> findByUser(User user);
    Optional<Employer> findByUserId(Long userId);

    Optional<Employer> findByUserEmail(String email);

}
