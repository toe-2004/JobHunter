package com.jobhunter.JobHunter.dto;

import com.jobhunter.JobHunter.model.Employer;
import com.jobhunter.JobHunter.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployerProfileDto {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Company email is required")
    @Email(message = "Please provide a valid company email address")
    private String companyEmail;

    @NotBlank(message = "Company phone is required")
    private String companyPhone;

    @NotBlank(message = "Company description is required")
    private String companyDescription;

    @NotBlank(message = "Company location is required")
    private String companyLocation;

    // User
    @NotBlank(message = "Your name is required")
    private String name;

    public static EmployerProfileDto from(User user, Employer employer) {

        EmployerProfileDto dto = new EmployerProfileDto();

        dto.setCompanyName(employer.getCompanyName());
        dto.setCompanyEmail(employer.getCompanyEmail());
        dto.setCompanyPhone(employer.getCompanyPhone());
        dto.setCompanyDescription(employer.getCompanyDescription());
        dto.setCompanyLocation(employer.getCompanyLocation());
        dto.setName(user.getName());

        return dto;
    }

}