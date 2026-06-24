package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jobhunter.JobHunter.model.User;


public interface UserRepository extends JpaRepository<User, Long>{

}
