package com.jobhunter.JobHunter.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.jobhunter.JobHunter.enumeration.Role;
import com.jobhunter.JobHunter.model.User;
import com.jobhunter.JobHunter.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String registerUser(User user, MultipartFile photo) {
        if(userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists!";
        }

        if(userRepository.existsByName(user.getName())) {
            return "Username already exists!";
        }
        user.setRole(Role.GUEST);
        if(photo != null && !photo.isEmpty()) {
            String fileName = photo.getOriginalFilename();
            try {
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);
                if(!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                 }
                Files.copy(
                    photo.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
                );
                user.setPassword(
                	    passwordEncoder.encode(user.getPassword())
                	);
                user.setProfilePhoto(fileName);

            } catch(IOException e) {
                throw new RuntimeException("Could not save image.", e);
          }
        }
        userRepository.save(user);
        return "success";
    }

}