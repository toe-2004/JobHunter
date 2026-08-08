package com.jobhunter.JobHunter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployerRegistrationDto {

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
}