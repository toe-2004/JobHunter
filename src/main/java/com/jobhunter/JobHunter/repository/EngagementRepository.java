package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.Engagement;

@Repository
public interface EngagementRepository extends JpaRepository<Engagement, Long> {

}
