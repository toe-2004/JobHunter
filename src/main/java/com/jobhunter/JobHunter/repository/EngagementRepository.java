package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jobhunter.JobHunter.model.Engagement;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {

}
