package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.JobSkill;

@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, Long>{


    @Modifying
    @Query("DELETE FROM JobSkill js WHERE js.job.id = :jobId")
    void deleteByJobId(@Param("jobId") Long jobId);
    
    
	
	
}
