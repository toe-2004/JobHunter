package com.jobhunter.JobHunter.config;

import com.jobhunter.JobHunter.model.Category;
import com.jobhunter.JobHunter.model.Skill;
import com.jobhunter.JobHunter.repository.CategoryRepository;
import com.jobhunter.JobHunter.repository.SkillRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepo;
    private final SkillRepository skillRepo;

    public DataSeeder(CategoryRepository categoryRepo, SkillRepository skillRepo) {
        this.categoryRepo = categoryRepo;
        this.skillRepo = skillRepo;
    }

    @Override
    public void run(String... args) {
        seedCategories();
        seedSkills();
    }

    private void seedCategories() {
        List<String> categories = List.of(
            "Web Development",
            "Mobile Development",
            "UI/UX Design",
            "Graphic Design",
            "Data Science",
            "Machine Learning",
            "DevOps & Cloud",
            "Cybersecurity",
            "Game Development",
            "Blockchain",
            "Content Writing",
            "SEO & Marketing",
            "Video Editing",
            "3D Modeling",
            "Database Administration"
        );

        for (String name : categories) {
            if (!categoryRepo.existsByName(name)) {
                Category c = new Category();
                c.setName(name);
                categoryRepo.save(c);
            }
        }
        System.out.println("Categories seeded.");
    }

    private void seedSkills() {
        List<String> skills = List.of(
            // Languages
            "Java", "Python", "JavaScript", "TypeScript", "PHP", "C#", "Go", "Rust", "Swift", "Kotlin",
            // Frontend
            "React", "Vue.js", "Angular", "HTML", "CSS", "Bootstrap", "Tailwind CSS",
            // Backend
            "Spring Boot", "Django", "Laravel", "Node.js", "Express.js", "FastAPI",
            // Mobile
            "Android", "iOS", "Flutter", "React Native",
            // Database
            "MySQL", "PostgreSQL", "MongoDB", "Redis", "Firebase",
            // DevOps
            "Docker", "Kubernetes", "AWS", "Azure", "CI/CD", "Linux",
            // Design
            "Figma", "Adobe XD", "Photoshop", "Illustrator",
            // Data
            "Machine Learning", "TensorFlow", "PyTorch", "SQL", "Data Analysis",
            // Other
            "Git", "REST API", "GraphQL", "Solidity", "WordPress"
        );

        for (String name : skills) {
            if (!skillRepo.existsByName(name)) {
                Skill s = new Skill();
                s.setName(name);
                skillRepo.save(s);
            }
        }
        System.out.println("Skills seeded.");
    }
}