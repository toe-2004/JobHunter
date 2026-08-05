package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jobhunter.JobHunter.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long>{

}
