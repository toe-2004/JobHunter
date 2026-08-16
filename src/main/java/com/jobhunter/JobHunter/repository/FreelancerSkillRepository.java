package com.jobhunter.JobHunter.repository;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.*;
@Repository
public interface FreelancerSkillRepository extends JpaRepository<FreelancerSkill, Long>{

	List<FreelancerSkill> findByFreelancer(Freelancer freelancer);

	 @Modifying
	    @Query("DELETE FROM FreelancerSkill fs WHERE fs.freelancer = :freelancer")
	    void deleteByFreelancer(
	            @Param("freelancer")
	            Freelancer freelancer);
	
}


