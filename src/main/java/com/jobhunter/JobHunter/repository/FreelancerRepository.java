package com.jobhunter.JobHunter.repository;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.*;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long>{

	Optional<Freelancer> findByUser(User user);

	Optional<Freelancer> findByUserId(Long userId);

	Optional<Freelancer> findByUserEmail(String email);
	
	List<Freelancer> findAll();
	
	 List<Freelancer> findTop4ByOrderByIdDesc();
	
	

}
