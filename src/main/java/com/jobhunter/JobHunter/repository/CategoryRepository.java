package com.jobhunter.JobHunter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jobhunter.JobHunter.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
