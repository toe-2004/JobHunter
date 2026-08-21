package com.jobhunter.JobHunter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobhunter.JobHunter.model.Category;
import com.jobhunter.JobHunter.model.Job;
import com.jobhunter.JobHunter.repository.CategoryRepository;
import com.jobhunter.JobHunter.repository.JobRepository;

@Controller
public class CategoryController {
	
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private JobRepository jobRepository;

	@GetMapping("/categories")
    public String allCategories(Model model) {

        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", "categories");
        return "category/all-categories";
    }
	 
    @GetMapping("/category-jobs")
    public String viewAvailableJobs(
            @RequestParam(required = false) Long categoryId,
            Model model) {

        List<Job> jobs;

        if (categoryId != null) {

            Category category = categoryRepository
                    .findById(categoryId)
                    .orElseThrow(() ->
                            new RuntimeException("Category not found"));

            jobs = jobRepository.findByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {

            jobs = jobRepository.findAll();
        }
        model.addAttribute("currentPage", "categories");
        model.addAttribute("jobs", jobs);

        return "category/jobs";
    }
}
