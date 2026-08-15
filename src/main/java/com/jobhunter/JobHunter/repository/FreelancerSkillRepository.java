package com.jobhunter.JobHunter.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jobhunter.JobHunter.model.Freelancer;
import com.jobhunter.JobHunter.model.FreelancerSkill;
import com.jobhunter.JobHunter.model.Skill;

@Repository
public interface FreelancerSkillRepository extends JpaRepository<FreelancerSkill, Long>{

	List<FreelancerSkill> findByFreelancer(Freelancer freelancer);

	 @Modifying
	    @Query("""
	        DELETE FROM FreelancerSkill fs
	        WHERE fs.freelancer = :freelancer
	    """)
	    void deleteByFreelancer(
	            @Param("freelancer")
	            Freelancer freelancer);
	
}


