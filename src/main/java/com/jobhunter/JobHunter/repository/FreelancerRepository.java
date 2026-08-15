package com.jobhunter.JobHunter.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhunter.JobHunter.model.Freelancer;
import com.jobhunter.JobHunter.model.User;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long>{

	Optional<Freelancer> findByUser(User user);

	

}
