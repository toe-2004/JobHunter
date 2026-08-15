package com.jobhunter.JobHunter.repository;

import com.jobhunter.JobHunter.model.Application;
import com.jobhunter.JobHunter.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findTop5ByJobEmployerOrderByCreatedAtDesc(Employer employer);
}
