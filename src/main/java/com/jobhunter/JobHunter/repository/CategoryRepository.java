package com.jobhunter.JobHunter.repository;


import com.jobhunter.JobHunter.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
