package com.jobhunter.JobHunter.service;

import com.jobhunter.JobHunter.dto.EmployerProfileDto;
import com.jobhunter.JobHunter.dto.EmployerRegistrationDto;
import com.jobhunter.JobHunter.model.Application;
import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.Job;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.ApplicationRepository;
import com.jobhunter.JobHunter.repository.EmployerRepository;
import com.jobhunter.JobHunter.repository.JobRepository;
import com.jobhunter.JobHunter.repository.UserRepository;
import com.jobhunter.JobHunter.enumeration.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class EmployerService {

    private final UserService userService;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    public EmployerService(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Employer getProfile() {

        User user = userService.getCurrentUser();
        Employer employer = user.getEmployer();

        if (employer == null) {
            throw new IllegalStateException(
                    "Current user does not have an employer profile");
        }
        return employer;
    }

    @Transactional(readOnly = true)
    public List<Job> getRecentJobs() {

        Employer employer = getProfile();
        return jobRepository
                .findTop5ByEmployerOrderByCreatedAtDesc(employer);
    }

    @Transactional(readOnly = true)
    public long getJobCount() {

        Employer employer = getProfile();
        return jobRepository.countByEmployer(employer);
    }

    @Transactional(readOnly = true)
    public List<Application> getRecentApplications() {

        Employer employer = getProfile();
        return applicationRepository.findTop5ByJobEmployerOrderByCreatedAtDesc(employer);
    }

    @Transactional
    public Employer createEmployer(Long userId, EmployerRegistrationDto form) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "User not found"));

        if (user.getEmployer() != null) {
            throw new IllegalStateException(
                    "Employer profile already exists");
        }

        if (employerRepository.existsByCompanyEmail(form.getCompanyEmail())) {
            throw new IllegalStateException(
                    "Company email already exists");
        }
        
        
        user.setRole(Role.EMPLOYER);
        Employer employer = new Employer();
        employer.setUser(user);
        employer.setCompanyName(form.getCompanyName());
        employer.setCompanyEmail(form.getCompanyEmail());
        employer.setCompanyPhone(form.getCompanyPhone());
        employer.setCompanyLocation(form.getCompanyLocation());
        employer.setCompanyDescription(form.getCompanyDescription());

        userRepository.save(user);
        return employerRepository.save(employer);
    }

//    @Transactional
//    public void updateProfile(EmployerProfileDto dto) {
//
//        Employer employer = getProfile();
//        User user = employer.getUser();
//        if (!employer.getCompanyEmail().equals(dto.getCompanyEmail()) && employerRepository.existsByCompanyEmail(
//                dto.getCompanyEmail())) {
//            throw new IllegalStateException(
//                    "Company email already exists");
//        }
//
//        employer.setCompanyName(dto.getCompanyName());
//        employer.setCompanyEmail(dto.getCompanyEmail());
//        employer.setCompanyPhone(dto.getCompanyPhone());
//        employer.setCompanyDescription(dto.getCompanyDescription());
//        employer.setCompanyLocation(dto.getCompanyLocation());
//        user.setName(dto.getName());
//        employerRepository.save(employer);
//        userRepository.save(user);
//    }
    
    public void updateProfile(EmployerProfileDto profileDto,MultipartFile profilePhoto) {

        Employer employer = getProfile();

        User user = employer.getUser();

        employer.setCompanyName(profileDto.getCompanyName());
        employer.setCompanyEmail(profileDto.getCompanyEmail());
        employer.setCompanyPhone(profileDto.getCompanyPhone());
        employer.setCompanyLocation(profileDto.getCompanyLocation());
        employer.setCompanyDescription(profileDto.getCompanyDescription());

        if (profilePhoto != null && !profilePhoto.isEmpty()) {

            String fileName = UUID.randomUUID()
                    + "_" + profilePhoto.getOriginalFilename();

            Path uploadPath = Paths.get("uploads/profile");

            try {

                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(fileName);

                Files.copy(
                        profilePhoto.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                user.setProfilePhoto(fileName);

            } catch (IOException e) {

                throw new IllegalStateException(
                        "Could not upload profile photo",e);
            }
        }

        employerRepository.save(employer);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Employer getCurrentEmployer() {

        User user = userService.getCurrentUser();
        return employerRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException(
                        "Employer profile not found"));
    }
    
    public List<Employer> getLatest4Employers() {
        return employerRepository.findTop4ByOrderByIdDesc();
    }

    public List<Employer> getAllEmployers() {
        return employerRepository.findAllByOrderByIdDesc();
    }

    public Employer getEmployerById(Long id) {
        return employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));
    }

}